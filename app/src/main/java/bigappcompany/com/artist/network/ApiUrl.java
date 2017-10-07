package bigappcompany.com.artist.network;

public final class ApiUrl {
        //private static final String BASE_URL = "http://cf4b3abc.ngrok.io/artist/";
        public static final String BASE_URL = "http://bigappcompany.co.in/demos/artist/";
        private static final String API_KEY = "rsi8197028387";
        public static final String SLIDERS = BASE_URL + "sliders/" + API_KEY;
        public static final String IMG_TYPES = BASE_URL + "image_types/" + API_KEY;
        public static final String IMAGES = BASE_URL + "artist_photos_list/";
        public static final String UPLOAD_IMG = BASE_URL + "artist_image_upload";
        public static final String UPLOAD_VID = BASE_URL + "artist_video_upload";
        public static final String CATEGORIES = BASE_URL + "category";
        public static final String NEWS = BASE_URL + "newsNnotifications/" + API_KEY+"/NEWS";
        public static final String BOTH = BASE_URL + "newsNnotifications/" + API_KEY+"/BOTH";
        public static final String VIDEOS = BASE_URL + "artist_videos_list/";
        public static final String EVENTS = BASE_URL + "event_details_artist/";
        public static final String LOGIN = BASE_URL + "artist_login";
        public static final String CITIES = BASE_URL + "city_list";
        public static final String REGISTER = BASE_URL + "artist_registration";
        public static final String UPDATE = BASE_URL + "update_artist_profile";
        public static final String UPDATE_EVENT = BASE_URL + "update_event_status";
        public static final String P_PIC = BASE_URL + "artist_profile_pic_update";
        public static final String PROFILE = BASE_URL + "artist_profile_details/";
        public static final String GETAMOUNT = BASE_URL + "registration_fees";


    private String unp="rsi:rsi8197028387";

        public String getUnp() {
                return unp;
        }
}
