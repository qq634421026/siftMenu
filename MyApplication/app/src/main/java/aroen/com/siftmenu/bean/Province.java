package aroen.com.siftmenu.bean;

import java.util.List;

/**
 * Created by zhanghongyu on 16/8/9.
 */
public class Province {
    private String provinceName;
    private List<String> cityList;

    public List<String> getCityList() {
        return cityList;
    }

    public void setCityList(List<String> cityList) {
        this.cityList = cityList;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public String toString() {
        return provinceName + cityList.toString();
    }
}
