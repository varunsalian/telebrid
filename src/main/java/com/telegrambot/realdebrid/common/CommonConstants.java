package com.telegrambot.realdebrid.common;


public final class CommonConstants {

    //HTTP REQUEST CONSTANTS
    public static final String HTTP_GET = "GET";
    public static final String HTTP_POST = "POST";
    public static final String USER_AGENT = "User-Agent";
    public static final String USER_AGENT_DETAILS = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";
    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String HTTP_REFERRER = "http://www.google.com";
    public static final String URL_ENCODED_CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String CHARSET = "application/x-www-form-urlencoded";
    public static final String UTF_8 = "utf-8";
    public static final String CONTENT_LENGTH = "Content-Length";

    //MAGNET SPECIFIC CONSTANTS
    public static final String MAGNET_URL = "magnet:?xt=urn:btih:";
    public static final String MAGNET_DOWNLOAD = "&dn=";
    public static final String MAGNET_TRACKER = "&tr=http://track.one:1234/announce&tr=udp://track.two:80";

    //USER INPUT CONSTANTS
    public static final String USER_INPUT_GOTO = "Goto ";
    public static final String USER_INPUT_ENTER_CODE = " and enter this code  ";

    //STRING CONSTANTS
    public static final String STRING_SPACE = " ";
    public static final String EMPTY_STRING = "";
    public static final String STRING_UNDERSCORE = "_";
    public static final String DOT_AND_SPACE = ". ";
    public static final String FORWARD_SLASH = "/";
    public static final String SYMBOL_EQUALS = "=";
    public static final String SYMBOL_AND = "&";
    public static final String SYMBOL_HYPHEN = "-";

    //REAL DEBRID SPECIFIC CONSTANTS
    public static final String DEBRID_OAUTH_URL = "https://api.real-debrid.com/oauth/v2";
    public static final String DEBRID_SECRET_ID_PATH = "/device/credentials?client_id=X245A4XAIBGVM&code=";
    public static final String DEBRID_TOKEN_PATH = "/token";
    public static final String DEBRID_AUTHENTICATION_PATH = "/device/code?client_id=X245A4XAIBGVM&new_credentials=yes";
    public static final String DEBRID_API_URL = "https://api.real-debrid.com/rest/1.0";
    public static final String DEBRID_TORRENT_INSTANT_AVAILABILITY_PATH = "/torrents/instantAvailability/";
    public static final String GRANT_TYPE_URL = "http://oauth.net/grant_type/device/1.0";
    public static final String MAGNET = "magnet";
    public static final String FILES = "files";
    public static final String LINK = "link";

    //ERROR CONSTANTS
    public static final String URL_INVALID = "INVALID URL";
    public static final String ERROR = "ERROR";
    public static final String ERROR_SESSION_TIMED_OUT = "SESSION TIMED OUT";
    public static final String ERROR_AUTHENTICATION_CONNECTION_FAILED = "Failed to connect to Authentication Server";

    //CLIENT POST
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String DEVICE_CODE = "code";
    public static final String GRANT_TYPE = "grant_type";

    //SUBTITLE SPECIFIC CONSTANTS
    public static final String SUBS_LANGUAGE = "language";
    public static final String YTS_SUBS_SOURCE = "data/ytssubs.json";
    public static final String SUBS_ENGLISH = "English";
    public static final String SUBS_ROOT_FOLDER = "support/subtitle";
    public static final String SUBS_COMPRESSED_FOLDER = "support/subtitle/compressed/";
    public static final String SUBS_UNCOMPRESSED_FOLDER = "support/subtitle/uncompressed/";
    public static final String SUBS_DOWNLOAD_LINK = "link";
    public static final String JSON_EXTENSION = ".json";
    public static final String ZIP_EXTENSION = ".zip";
    public static final String SRT_EXTENSION = ".srt";
    public static final String SUBS_DOWNLOAD_SELECTOR = "a[href].btn-icon";
    public static final String SELECTOR_HREF = "href";


    //EXCEPTION MESSAGES
    public static final String UNABLE_TO_CONNECT = "Unable to connect ";

    //BOT CONSTANTS
    public static final String CHAT_HISTORY = "chatHistory";
    public static final String MESSAGE_ENTER_MAGNET_URL ="Enter the magnet URL";

    //OPTIONS
    public static final String OPTION_LOGIN = "/login";
    public static final String OPTION_MAGNET = "/magnet";

    private CommonConstants() {

    }
}
