package cn.qingyuyu.yulauncher;
import android.widget.*;
import android.content.*;
import java.util.*;
import android.view.*;

public class MyGridViewAdapter extends BaseAdapter 
{
	private Context context;
	private List<AppInfo> listitem; 
	public MyGridViewAdapter(Context context,List<AppInfo> listitem) 
	{ 
		this.context = context; 
		this.listitem = listitem; 
	} 
	@Override 
	public int getCount() 
	{ 
		return listitem.size(); 
	} 
	@Override 
	public Object getItem(int position) 
	{
		return listitem.get(position);
	} 
	@Override 
	public long getItemId(int position)
	{
		return position; 
	} 
	@Override 
	public View getView(int position, View convertView, ViewGroup parent)
 {
	 if (convertView == null)
 		{ 
		convertView = LayoutInflater.from(context).inflate(R.layout.item, null); 
		} 
		ImageView imageView = (ImageView) convertView.findViewById(R.id.image); 
		TextView textView = (TextView) convertView.findViewById(R.id.text); 
		AppInfo app =  listitem.get(position); 
		imageView.setImageDrawable(app.getIcon()); 
		textView.setText(app.getAppName());
		return convertView; 
		} 
		}
