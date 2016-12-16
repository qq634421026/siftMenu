package aroen.com.siftmenu;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aroen.com.siftmenu.adapter.FirstLevelAdapter;
import aroen.com.siftmenu.adapter.SecondLevelAdapter;
import aroen.com.siftmenu.bean.Province;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context = this;
    private RelativeLayout expertListTop;
    private RelativeLayout layout_expert_area;
    private RelativeLayout layout_expert_territory;
    private RelativeLayout layout_expert_service;
    private RelativeLayout layout_expert_sort;
    private RelativeLayout expert_list_loading;
    private LinearLayout expert_selector_root;
    private TextView expertArea;
    private TextView expertTerritory;
    private TextView expertService;
    private TextView expertSort;
    private ListView expertListview;
    private EditText expert_list_top_edittext;
    private List<String> service_list = new ArrayList<String>();
    private List<String> sort_list = new ArrayList<String>();
    private String territory = "";
    private PopupWindow areapw;
    private PopupWindow territorypw;
    private PopupWindow servicepw;
    private PopupWindow sortpw;
    private View ServicePwView;
    private View SortPwView;
    private View TerritoryView;
    private View AreaView;
    private ListView pwservice_list;
    private ListView pwsort_list;

    private ListView select_first_level_listview;
    private ListView select_second_level_listview;

    private ListView area_second_level_listview;
    private ListView area_first_level_listview;

    private SecondLevelAdapter serviceAdapter;
    private SecondLevelAdapter sortAdapter;
    private float density;

    private boolean sortFalg = false;
    private boolean serviceFalg = false;
    private boolean areaFalg = false;
    private boolean territoryFalg = false;

    private HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
    private HashMap<Integer, Boolean> areaisSelected = new HashMap<Integer, Boolean>();
    private List<String> first_level_list = new ArrayList<String>();
    private Map<String, List<String>> second_map = new HashMap<String, List<String>>();
    private List<String> second_level_list = new ArrayList<String>();

    private List<String> area_first_level_list = new ArrayList<String>();
    private List<String> area_second_level_list = new ArrayList<String>();

    private FirstLevelAdapter first_adapter;
    private SecondLevelAdapter second_adapter;

    private FirstLevelAdapter area_first_adapter;
    private SecondLevelAdapter area_second_adapter;

    private int index = 0;
    private int index2 = 0;

    private LinearLayout select_two_root;
    private LinearLayout area_two_root;
    private List<Province> provinceList;
    private int pageNumber = 0;

    private String cityStr = "";
    private String goodfieldStr = "";
    private String supportserviceStr = "";
    private String ordersStr = "";

    private String keyWord = "";
    private TextView expert_list_empty;

    private String province = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        initData();
        initView();
        initServicePw();
        initSort();
        initTerritorypw();
        initArea();

    }

    private void initArea() {
        AreaView = getLayoutInflater().inflate(R.layout.twolevle_pop, null);
        areapw = new PopupWindow(AreaView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        areapw.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.expert_pop_bg));
        areapw.setFocusable(false);//可以试试设为false的结果
        areapw.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
        areapw.setTouchable(true);
        area_first_level_listview = (ListView) AreaView.findViewById(R.id.select_first_level_listview);
        area_second_level_listview = (ListView) AreaView.findViewById(R.id.select_second_level_listview);
        area_two_root = (LinearLayout) AreaView.findViewById(R.id.select_two_root);
        area_two_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areapw.dismiss();
                areaFalg = true;
                expertArea.setTextColor(Color.parseColor("#bdbdbd"));
                expertSort.setTextColor(Color.parseColor("#bdbdbd"));
                expertTerritory.setTextColor(Color.parseColor("#bdbdbd"));
                expertService.setTextColor(Color.parseColor("#bdbdbd"));
            }
        });
        area_second_level_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expertArea.setText(provinceList.get(index2).getCityList().get(position));
                areapw.dismiss();
                areaFalg = true;
                expertArea.setTextColor(Color.parseColor("#bdbdbd"));
                expertSort.setTextColor(Color.parseColor("#bdbdbd"));
                expertTerritory.setTextColor(Color.parseColor("#bdbdbd"));
                expertService.setTextColor(Color.parseColor("#bdbdbd"));
                cityStr = provinceList.get(index2).getCityList().get(position);
                province = provinceList.get(index2).getProvinceName();
            }
        });
        area_first_adapter = new FirstLevelAdapter(area_first_level_list, context, areaisSelected);
        area_first_level_listview.setAdapter(area_first_adapter);
        area_first_level_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                for (int i = 0; i < area_first_level_list.size(); i++) {
                    areaisSelected.put(i, false);
                }
                areaisSelected.put(position, true);
                area_first_adapter.notifyDataSetChanged();
                area_second_level_list.clear();
                area_second_level_list.addAll(provinceList.get(position).getCityList());
                area_second_adapter.notifyDataSetChanged();
                index2 = position;
            }
        });
        area_second_adapter = new SecondLevelAdapter(area_second_level_list, context);
        area_second_level_listview.setAdapter(area_second_adapter);
    }

    private void initTerritorypw() {
        TerritoryView = getLayoutInflater().inflate(R.layout.twolevle_pop, null);
        territorypw = new PopupWindow(TerritoryView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        territorypw.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.expert_pop_bg));
        territorypw.setFocusable(false);//可以试试设为false的结果
        territorypw.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
        territorypw.setTouchable(true);
        select_first_level_listview = (ListView) TerritoryView.findViewById(R.id.select_first_level_listview);
        select_second_level_listview = (ListView) TerritoryView.findViewById(R.id.select_second_level_listview);
        select_two_root = (LinearLayout) TerritoryView.findViewById(R.id.select_two_root);
        select_two_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                territorypw.dismiss();
                territoryFalg = true;
                expertArea.setTextColor(Color.parseColor("#bdbdbd"));
                expertSort.setTextColor(Color.parseColor("#bdbdbd"));
                expertTerritory.setTextColor(Color.parseColor("#bdbdbd"));
                expertService.setTextColor(Color.parseColor("#bdbdbd"));
            }
        });
        select_second_level_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expertTerritory.setText(second_map.get(first_level_list.get(index)).get(position));
                goodfieldStr = second_map.get(first_level_list.get(index)).get(position);
                territorypw.dismiss();
                territoryFalg = true;
                expertArea.setTextColor(Color.parseColor("#bdbdbd"));
                expertSort.setTextColor(Color.parseColor("#bdbdbd"));
                expertTerritory.setTextColor(Color.parseColor("#bdbdbd"));
                expertService.setTextColor(Color.parseColor("#bdbdbd"));
            }
        });
        first_adapter = new FirstLevelAdapter(first_level_list, context, isSelected);
        select_first_level_listview.setAdapter(first_adapter);
        select_first_level_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                for (int i = 0; i < first_level_list.size(); i++) {
                    isSelected.put(i, false);
                }
                isSelected.put(position, true);
                first_adapter.notifyDataSetChanged();
                second_level_list.clear();
                second_level_list.addAll(second_map.get(first_level_list.get(position)));
                second_adapter.notifyDataSetChanged();
                index = position;
            }
        });
        second_adapter = new SecondLevelAdapter(second_level_list, context);
        select_second_level_listview.setAdapter(second_adapter);
    }

    private void initSort() {
        SortPwView = getLayoutInflater().inflate(R.layout.pwservice, null);
        sortpw = new PopupWindow(SortPwView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        sortpw.setBackgroundDrawable(getResources().getDrawable(
                //此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
                R.drawable.expert_pop_bg));
        //设置焦点为可点击
        sortpw.setFocusable(false);//可以试试设为false的结果
        sortpw.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
        sortpw.setTouchable(true);
        pwsort_list = (ListView) SortPwView.findViewById(R.id.pwservice_list);
        LinearLayout root = (LinearLayout) SortPwView.findViewById(R.id.pop_root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortpw.dismiss();
                sortFalg = true;
                expertArea.setTextColor(Color.parseColor("#bdbdbd"));
                expertSort.setTextColor(Color.parseColor("#bdbdbd"));
                expertTerritory.setTextColor(Color.parseColor("#bdbdbd"));
                expertService.setTextColor(Color.parseColor("#bdbdbd"));
            }
        });
        sortAdapter = new SecondLevelAdapter(sort_list, context);
        pwsort_list.setAdapter(sortAdapter);
        pwsort_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expertSort.setText(sort_list.get(position));
                sortpw.dismiss();
                sortFalg = true;
                expertArea.setTextColor(Color.parseColor("#bdbdbd"));
                expertSort.setTextColor(Color.parseColor("#bdbdbd"));
                expertTerritory.setTextColor(Color.parseColor("#bdbdbd"));
                expertService.setTextColor(Color.parseColor("#bdbdbd"));
                if (sort_list.get(position).equals("智能排序")) {
                    ordersStr = "1";
                } else if (sort_list.get(position).equals("评价最高")) {
                    ordersStr = "2";
                } else if (sort_list.get(position).equals("发明专利数量")) {
                    ordersStr = "3";
                } else if (sort_list.get(position).equals("发明授权率")) {
                    ordersStr = "4";
                } else {
                    ordersStr = "5";
                }
            }
        });
    }

    private void initServicePw() {
        ServicePwView = getLayoutInflater().inflate(R.layout.pwservice, null);
        servicepw = new PopupWindow(ServicePwView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        servicepw.setBackgroundDrawable(getResources().getDrawable(
                //此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
                R.drawable.expert_pop_bg));
        //设置焦点为可点击
        servicepw.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
        servicepw.setFocusable(false);//可以试试设为false的结果
        servicepw.setTouchable(true);
        pwservice_list = (ListView) ServicePwView.findViewById(R.id.pwservice_list);
        LinearLayout root = (LinearLayout) ServicePwView.findViewById(R.id.pop_root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicepw.dismiss();
                serviceFalg = true;
                expertArea.setTextColor(Color.parseColor("#bdbdbd"));
                expertSort.setTextColor(Color.parseColor("#bdbdbd"));
                expertTerritory.setTextColor(Color.parseColor("#bdbdbd"));
                expertService.setTextColor(Color.parseColor("#bdbdbd"));
            }
        });
        serviceAdapter = new SecondLevelAdapter(service_list, context);
        pwservice_list.setAdapter(serviceAdapter);
        pwservice_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expertService.setText(service_list.get(position));
                supportserviceStr = service_list.get(position);
                servicepw.dismiss();
                serviceFalg = true;
                expertArea.setTextColor(Color.parseColor("#bdbdbd"));
                expertSort.setTextColor(Color.parseColor("#bdbdbd"));
                expertTerritory.setTextColor(Color.parseColor("#bdbdbd"));
                expertService.setTextColor(Color.parseColor("#bdbdbd"));
            }
        });
    }

    private void initData() {
        service_list.add("国内专利申请");
        service_list.add("涉外专利申请");
        service_list.add("答复审查意见");
        service_list.add("专利复审");
        service_list.add("专利无效");
        service_list.add("侵权诉讼");
        service_list.add("行政诉讼");
        service_list.add("知识产权管理咨询");
        service_list.add("专利检索");
        service_list.add("专利分析");

        sort_list.add("智能排序");
        sort_list.add("评价最高");
        sort_list.add("发明专利数量");
        sort_list.add("发明授权率");
        sort_list.add("平均授权时长");

        first_level_list.add("生活必需");
        first_level_list.add("电学");
        first_level_list.add("物理");
        first_level_list.add("化学");
        first_level_list.add("机械");
        first_level_list.add("作业运输");
        first_level_list.add("建筑业");
        first_level_list.add("纺织造纸");
        isSelected.put(0, true);
        for (int i = 1; i < first_level_list.size(); i++) {
            isSelected.put(i, false);
        }
        List<String> list1 = new ArrayList<String>();
        list1.add("家用设备");
        list1.add("医学卫生");
        list1.add("手携物品");
        list1.add("食品加工");
        list1.add("救生消防");
        list1.add("运动娱乐");
        list1.add("农林牧渔");
        list1.add("服装鞋帽");
        list1.add("文教美工");
        list1.add("珠宝烟草");
        list1.add("其他");
        second_level_list.addAll(list1);
        List<String> list2 = new ArrayList<String>();
        list2.add("电气元件");
        list2.add("发电配电");
        list2.add("电子电路");
        list2.add("电器制造");
        list2.add("通信技术");
        list2.add("互联网");
        list2.add("计算机");
        list2.add("其他");
        List<String> list3 = new ArrayList<String>();
        list3.add("测量测试");
        list3.add("计算核算");
        list3.add("摄影电影");
        list3.add("光学");
        list3.add("信号装置");
        list3.add("教育广告");
        list3.add("乐器声学");
        list3.add("仪器零部件");
        list3.add("核工程");
        list3.add("其他");
        List<String> list4 = new ArrayList<String>();
        list4.add("无机化学");
        list4.add("有机化学");
        list4.add("分子生物学");
        list4.add("制药工程");
        list4.add("冶金矿物");
        list4.add("石油化工");
        list4.add("材料化学");
        list4.add("其他");
        List<String> list5 = new ArrayList<String>();
        list5.add("发动机");
        list5.add("液压气动");
        list5.add("照明");
        list5.add("供热通风");
        list5.add("加热制冷");
        list5.add("烘烤蒸馏");
        list5.add("蒸汽燃烧");
        list5.add("武器弹药");
        list5.add("其他");

        List<String> list6 = new ArrayList<String>();
        list6.add("分离装置");
        list6.add("清洁");
        list6.add("土壤再生");
        list6.add("金属加工");
        list6.add("塑料水泥");
        list6.add("办公用品");
        list6.add("机动车");
        list6.add("铁路");
        list6.add("船舶");
        list6.add("航空航天");
        list6.add("输送贮存");
        list6.add("其他");
        List<String> list7 = new ArrayList<String>();
        list7.add("道路桥梁");
        list7.add("水利工程");
        list7.add("给水排水");
        list7.add("一般建筑");
        list7.add("钥匙锁");
        list7.add("门窗梯子");
        list7.add("采矿");
        list7.add("其他");
        List<String> list8 = new ArrayList<String>();
        list8.add("纤维纺纱");
        list8.add("编织缝纫");
        list8.add("织物洗涤");
        list8.add("绳缆索");
        list8.add("造纸");
        list8.add("其他");
        second_map.put(first_level_list.get(0), list1);
        second_map.put(first_level_list.get(1), list2);
        second_map.put(first_level_list.get(2), list3);
        second_map.put(first_level_list.get(3), list4);
        second_map.put(first_level_list.get(4), list5);
        second_map.put(first_level_list.get(5), list6);
        second_map.put(first_level_list.get(6), list7);
        second_map.put(first_level_list.get(7), list8);

        provinceList = getProvinceList(context);
        for (int i = 0; i < provinceList.size(); i++) {
            area_first_level_list.add(provinceList.get(i).getProvinceName());
        }
        area_second_level_list.addAll(provinceList.get(0).getCityList());
        areaisSelected.put(0, true);
        for (int i = 1; i < area_first_level_list.size(); i++) {
            areaisSelected.put(i, false);
        }
    }

    public static List<Province> getProvinceList(Context context) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(context.getAssets().open("city.xml"), "UTF-8");
            int eventType = parser.getEventType();
            List<Province> listProvince = null;
            List<String> listCity = null;
            Province province = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:
                        if ("citylist".equals(tagName)) {
                            listProvince = new ArrayList<Province>();
                        } else if ("pn".equals(tagName)) {
                            province = new Province();
                            province.setProvinceName(parser.nextText());
                            listCity = new ArrayList<String>();
                        } else if ("cn".equals(tagName)) {
//                            listCity.add(parser.nextText());
                        }
                    case XmlPullParser.END_TAG:
                        if ("pn".equals(tagName)) {
                            listProvince.add(province);
                        } else if ("cn".equals(tagName)) {
                            listCity.add(parser.nextText());
                            province.setCityList(listCity);
                        }
                        break;
                }
                eventType = parser.next();
            }
            return listProvince;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initView() {
        layout_expert_area = (RelativeLayout) findViewById(R.id.layout_expert_area);
        layout_expert_territory = (RelativeLayout) findViewById(R.id.layout_expert_territory);
        layout_expert_service = (RelativeLayout) findViewById(R.id.layout_expert_service);
        layout_expert_sort = (RelativeLayout) findViewById(R.id.layout_expert_sort);

        layout_expert_area.setOnClickListener(this);
        layout_expert_territory.setOnClickListener(this);
        layout_expert_service.setOnClickListener(this);
        layout_expert_sort.setOnClickListener(this);

        expert_selector_root = (LinearLayout) findViewById(R.id.expert_selector_root);

        expertArea = (TextView) findViewById(R.id.expert_area);
        expertTerritory = (TextView) findViewById(R.id.expert_territory);
        expertService = (TextView) findViewById(R.id.expert_service);
        expertSort = (TextView) findViewById(R.id.expert_sort);
        expertListview = (ListView) findViewById(R.id.expert_listview);
        expertArea.setOnClickListener(this);
        expertTerritory.setOnClickListener(this);
        expertService.setOnClickListener(this);
        expertSort.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.layout_expert_area:
                expertService.setTextColor(Color.parseColor("#bdbdbd"));
                expertSort.setTextColor(Color.parseColor("#bdbdbd"));
                expertTerritory.setTextColor(Color.parseColor("#bdbdbd"));
                sortFalg = false;
                serviceFalg = false;
                territoryFalg = false;
                if (!areaFalg) {
                    expertArea.setTextColor(Color.parseColor("#595959"));
                    areapw.showAsDropDown(expert_selector_root);
                    areaFalg = true;
                } else {
                    areapw.dismiss();
                    expertArea.setTextColor(Color.parseColor("#bdbdbd"));
                    areaFalg = false;
                }
                break;
            case R.id.layout_expert_sort:
                expertArea.setTextColor(Color.parseColor("#bdbdbd"));
                expertService.setTextColor(Color.parseColor("#bdbdbd"));
                expertTerritory.setTextColor(Color.parseColor("#bdbdbd"));
                serviceFalg = false;
                areaFalg = false;
                territoryFalg = false;
                if (!sortFalg) {
                    sortpw.showAsDropDown(expert_selector_root);
                    expertSort.setTextColor(Color.parseColor("#595959"));
                    sortFalg = true;
                } else {
                    sortpw.dismiss();
                    expertSort.setTextColor(Color.parseColor("#bdbdbd"));
                    sortFalg = false;
                }
                break;
            case R.id.layout_expert_territory:
                expertArea.setTextColor(Color.parseColor("#bdbdbd"));
                expertService.setTextColor(Color.parseColor("#bdbdbd"));
                expertSort.setTextColor(Color.parseColor("#bdbdbd"));
                areaFalg = false;
                sortFalg = false;
                serviceFalg = false;
                if (!territoryFalg) {
                    expertTerritory.setTextColor(Color.parseColor("#595959"));
                    territorypw.showAsDropDown(expert_selector_root);
                    territoryFalg = true;
                } else {
                    territorypw.dismiss();
                    expertTerritory.setTextColor(Color.parseColor("#bdbdbd"));
                    territoryFalg = false;
                }
                break;
            case R.id.layout_expert_service:
                expertArea.setTextColor(Color.parseColor("#bdbdbd"));
                expertSort.setTextColor(Color.parseColor("#bdbdbd"));
                expertTerritory.setTextColor(Color.parseColor("#bdbdbd"));
                sortFalg = false;
                areaFalg = false;
                territoryFalg = false;
                if (!serviceFalg) {
                    servicepw.showAsDropDown(expert_selector_root);
                    expertService.setTextColor(Color.parseColor("#595959"));
                    serviceFalg = true;
                } else {
                    servicepw.dismiss();
                    expertService.setTextColor(Color.parseColor("#bdbdbd"));
                    serviceFalg = false;
                }
                break;
            case R.id.expert_area:
                expertService.setTextColor(Color.parseColor("#bdbdbd"));
                expertSort.setTextColor(Color.parseColor("#bdbdbd"));
                expertTerritory.setTextColor(Color.parseColor("#bdbdbd"));
                sortFalg = false;
                serviceFalg = false;
                territoryFalg = false;
                if (!areaFalg) {
                    areapw.showAsDropDown(expert_selector_root);
                    expertArea.setTextColor(Color.parseColor("#595959"));
                    areaFalg = true;
                } else {
                    areapw.dismiss();
                    expertArea.setTextColor(Color.parseColor("#bdbdbd"));
                    areaFalg = false;
                }
                break;
            case R.id.expert_sort:
                expertArea.setTextColor(Color.parseColor("#bdbdbd"));
                expertService.setTextColor(Color.parseColor("#bdbdbd"));
                expertTerritory.setTextColor(Color.parseColor("#bdbdbd"));
                serviceFalg = false;
                areaFalg = false;
                territoryFalg = false;
                if (!sortFalg) {
                    sortpw.showAsDropDown(expert_selector_root);
                    expertSort.setTextColor(Color.parseColor("#595959"));
                    sortFalg = true;
                } else {
                    sortpw.dismiss();
                    expertSort.setTextColor(Color.parseColor("#bdbdbd"));
                    sortFalg = false;
                }
                break;
            case R.id.expert_territory:
                expertArea.setTextColor(Color.parseColor("#bdbdbd"));
                expertService.setTextColor(Color.parseColor("#bdbdbd"));
                expertSort.setTextColor(Color.parseColor("#bdbdbd"));
                areaFalg = false;
                sortFalg = false;
                serviceFalg = false;
                if (!territoryFalg) {
                    territorypw.showAsDropDown(expert_selector_root);
                    expertTerritory.setTextColor(Color.parseColor("#595959"));
                    territoryFalg = true;
                } else {
                    territorypw.dismiss();
                    expertTerritory.setTextColor(Color.parseColor("#bdbdbd"));
                    territoryFalg = false;
                }
                break;
            case R.id.expert_service:
                expertArea.setTextColor(Color.parseColor("#bdbdbd"));
                expertSort.setTextColor(Color.parseColor("#bdbdbd"));
                expertTerritory.setTextColor(Color.parseColor("#bdbdbd"));
                sortFalg = false;
                areaFalg = false;
                territoryFalg = false;
                if (!serviceFalg) {
                    servicepw.showAsDropDown(expert_selector_root);
                    expertService.setTextColor(Color.parseColor("#595959"));
                    serviceFalg = true;
                } else {
                    servicepw.dismiss();
                    expertService.setTextColor(Color.parseColor("#bdbdbd"));
                    serviceFalg = false;
                }
                break;
        }
    }

}
