package com.ycr.slidemenu;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ycr.pojo.App;
import com.ycr.pojo.HttpResultList;
import com.ycr.pojo.MessageInfo;
import com.ycr.util.MyFragment;
import com.ycr.util.PublicJsonParse;
import com.ycr.view.MyListGView;
import com.ycr.view.MyListGView.OnRefreshListener;
import com.indoorclub.girl.R;
/**
 * ListView
 * @author Administrator
 *
 */
public class FragmentXx extends MyFragment{
	private List<MessageInfo> dataList=new ArrayList<MessageInfo>();
	private BaseAdapter adapter;
	private SharedPreferences sharedPreferences;
	public FragmentXx(){}
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_xx, container, false);
    	sharedPreferences=getActivity().getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);//共享数据
    	getNotice();
    	final MyListGView listView = (MyListGView) view.findViewById(R.id.listView);
		adapter = new BaseAdapter() {
			public View getView(int position, View convertView, ViewGroup parent) {
				 MessageInfo mess=dataList.get(position);
				 convertView=LayoutInflater.from(getActivity()).inflate(R.layout.xx_item, null);
				 TextView textView = (TextView) convertView.findViewById(R.id.messageid);
				 textView.setText(mess.getNoticeId());
				 TextView textView1 = (TextView) convertView.findViewById(R.id.name);
				 textView1.setText(mess.getNoticeMessage());
				 TextView textView2 = (TextView) convertView.findViewById(R.id.date);
				 String date=mess.getNoticeTime();
				 if(date!=null&&!date.equals(""))
					 date=date.substring(8,10)+":"+date.substring(10,12);
				 textView2.setText(date);
				return convertView;
			}

			public long getItemId(int position) {
				return position;
			}

			public Object getItem(int position) {
				return dataList.get(position);
			}

			public int getCount() {
				return dataList.size();
			}
		};
		listView.setAdapter(adapter);
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						//dataList.addFirst("刷新后的内容");
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						adapter.notifyDataSetChanged();
						listView.onRefreshComplete();
					}

				}.execute(null,null,null);
			}
		});
    	return view;
    }
     private void getNotice() {
  		RequestParams params = new RequestParams();
  		try {
  			JSONObject json=new JSONObject();
  			json.put("accountId", sharedPreferences.getString("id", ""));
  			json.put("request", PublicJsonParse.getPublicParam("getNotice"));
  			params.addHeader("Content-Type", "application/json");
  			params.setBodyEntity(new StringEntity(json.toString()));
  		} catch (JSONException e) {
  			e.printStackTrace(); 
  		}
  		catch (UnsupportedEncodingException e) {
  			e.printStackTrace();
  		}
  		HttpUtils http = new HttpUtils();
  		http.configTimeout(30000);
  		http.send(HttpRequest.HttpMethod.POST,App.service+"getNotice",params,
  		    new RequestCallBack<String>(){

  		        @Override
  		        public void onStart() {
  		        }

  		        @Override
  		        public void onLoading(long total, long current, boolean isUploading) {
  		            
  		        }
  		        @Override
  		        public void onSuccess(ResponseInfo<String> responseInfo) {
  		        	Gson gson=new GsonBuilder().serializeNulls().create();
  		        	Type typeToken = new TypeToken<HttpResultList<MessageInfo>>(){}.getType();
  		        	String jsonElement=responseInfo.result.replace("[]", "null");
  		        	HttpResultList<MessageInfo> result=gson.fromJson(jsonElement,typeToken);
  		        	if(result.isResult()){
  		        		dataList=result.getData();
  		        		dataList=dataList==null?new ArrayList<MessageInfo>():dataList;
  		        		/*MessageInfo i=new MessageInfo();
  		        		i.setNoticeId("1");
  		        		i.setNoticeMessage("今天要去开会记得朝气啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
  		        		i.setNoticeTime("06/11 10:50");
  		        		MessageInfo b=new MessageInfo();
  		        		b.setNoticeId("1");
  		        		b.setNoticeMessage("我好喜欢你我要跟你表白");
  		        		b.setNoticeTime("06/11 10:50");
  		        		MessageInfo c=new MessageInfo();
  		        		c.setNoticeId("1");
  		        		c.setNoticeMessage("aaaaaaaaaaaaa今天要去开会记得朝气啊啊啊啊啊");
  		        		c.setNoticeTime("06/11 10:50");
  		        		dataList.add(c);
  		        		dataList.add(i);
  		        		dataList.add(b);*/
  		        		adapter.notifyDataSetChanged();
  		        	}else{
  		        		Toast toast = Toast.makeText(getActivity().getApplicationContext(),result.getError(), Toast.LENGTH_SHORT);
  						toast.setGravity(Gravity.CENTER, 0, 0);
  						toast.show();
  		        	}
  		        }

  		        @Override
  		        public void onFailure(HttpException error, String msg) {
  		        	Toast toast = Toast.makeText(getActivity().getApplicationContext(),msg, Toast.LENGTH_SHORT);
  					toast.setGravity(Gravity.CENTER, 0, 0);
  					toast.show();
  		        }
  		});
  	}
}
