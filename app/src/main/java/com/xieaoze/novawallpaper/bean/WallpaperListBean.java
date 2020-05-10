package com.xieaoze.novawallpaper.bean;

import java.util.List;

public class WallpaperListBean {
    /*{
            "title": "",
            "total": 7,
            "paged": 1,
            "perPage": 1,
            "data": [
                {
                        "id": 22,
                        "img": "http://img2.imgtn.bdimg.com/it/u=3594914547,4242157377&fm=26&gp=0.jpg",
                        "title": "7"
                }
	        ]
    }*/
    public String title;
    public int total;
    public int paged;
    public int perPage;
    public List<DataBean> data;

    public static class DataBean {
        public int id;
        public String img;
        public String title;
    }
}
