package com.ku6.cdn.dispatcher.common;

import java.util.Set;

import com.google.common.collect.Sets;

public final class Constrants {

	/**
	 * Table Constrants, including column name
	 */
	
	public static final Long GLOBAL_MOD = 10000L;
	
	// tb_server_info column name
	public static final String SERVER_SVR_ID = "svr_id";
	public static final String SERVER_SVR_TYPE = "svr_type";
	public static final String SERVER_NODE_ID = "node_id";
	public static final String SERVER_GROUP_ID = "group_id";
	public static final String SERVER_IP = "ip";
	public static final String SERVER_IP2 = "ip2";
	public static final String SERVER_IN_USE = "in_use";
	public static final String SERVER_ADD_TIME = "add_time";
	public static final String SERVER_MODIFY_TIME = "modify_time";
	public static final String SERVER_DO_SCAN_NGINX = "do_scan_nginx";
	public static final String SERVER_NGINX_VERSION = "nginx_version";
	public static final String SERVER_NGINX_STATUS = "nginx_status";
	public static final String SERVER_LIVE_STATUS = "live_status";
	public static final String SERVER_NGINX_UPDATE_TIME = "nginx_update_time";
	public static final String SERVER_DISP_CLINET_VERSION = "disp_client_version";
	public static final String SERVER_DISP_STATUS = "disp_status";
	public static final String SERVER_DISP_UPDATE_TIME = "disp_update_time";
	public static final String SERVER_REMARK = "remark";
	public static final String SERVER_LOAD_AVG = "load_avg";
	public static final String SERVER_ROOT_USG = "root_usg";
	public static final String SERVER_IS_CACHE = "is_cache";

	// tb_disk_info column name
	public static final String DISK_ID = "id";
	public static final String DISK_DISK_ID = "disk_id";
	public static final String DISK_GSLB_DISK_ID = "gslb_disk_id";
	public static final String DISK_SVR_ID = "svr_id";
	public static final String DISK_NODE_ID = "node_id";
	public static final String DISK_IN_USE = "in_use";
	public static final String DISK_DISK_STATUS = "disk_status";
	public static final String DISK_DISK_ERRNO = "disk_errno";
	public static final String DISK_PORT = "port";
	public static final String DISK_ROOT_DIR = "root_dir";
	public static final String DISK_MOUNT = "mount";
	public static final String DISK_DISK_SIZE = "disk_size";
	public static final String DISK_USED_SIZE = "used_size";
	public static final String DISK_INODE_TOTAL = "inode_total";
	public static final String DISK_INODE_USED = "inode_used";
	public static final String DISK_UPDATE_TIME = "update_time";
	public static final String DISK_IS_CACHE = "is_cache";

	// tb_group_info column name
	public static final String GROUP_ID = "id";
	public static final String GROUP_GROUP_ID = "group_id";
	public static final String GROUP_GROUP_NAME = "group_name";
	public static final String GROUP_GROUP_TYPE = "group_type";
	public static final String GROUP_IN_USE = "in_use";
	public static final String GROUP_ISP_ID = "isp_id";
	public static final String GROUP_ADD_TIME = "add_time";
	public static final String GROUP_MODIFY_TIME = "modify_time";
	public static final String GROUP_REMARK = "remark";

	// Task column name
	public static final String TASK_TASK_ID = "task_id";
	public static final String TASK_TASK_CREATE_TIME = "task_create_time";
	public static final String TASK_VID = "vid";
	public static final String TASK_SID = "sid";
	public static final String TASK_UID = "uid";
	public static final String TASK_SRC_DOMAIN = "src_domain";
	public static final String TASK_SRC_PATH = "src_path";
	public static final String TASK_UPLOAD_SERVER = "upload_server";
	public static final String TASK_DEST_DOMAIN = "dest_domain";
	public static final String TASK_PRIORITY = "priority";
	public static final String TASK_VIDEO_BITRATE = "video_bitrate";
	public static final String TASK_AUDIO_BITRATE = "audio_bitrate";
	public static final String TASK_WIDTH = "width";
	public static final String TASK_FPS = "fps";
	public static final String TASK_ENCODE_TYPE = "encode_type";
	public static final String TASK_ENCODE_PREFER = "encode_prefer";
	public static final String TASK_EXTRA_ARGS = "extra_args";
	public static final String TASK_REGULATION = "regulation";
	public static final String TASK_WATERMARK = "watermark";
	public static final String TASK_SERVER_PREFER = "server_prefer";
	public static final String TASK_TRANSCODE_SERVER = "transcode_server";
	public static final String TASK_TRANSCODE_START_TIME = "transcode_start_time";
	public static final String TASK_TRANSCODE_FINISH_TIME = "transcode_finish_time";
	public static final String TASK_TRANSCODE_STATUS = "transcode_status";
	public static final String TASK_TRANSCODE_STATUS_DESC = "transcode_status_desc";
	public static final String TASK_PIC_PATH = "pic_path";
	public static final String TASK_BIGPIC_PATH = "bigpic_path";
	public static final String TASK_SEG_COUNT = "seg_count";
	public static final String TASK_PLAY_TIME = "play_time";
	public static final String TASK_SEG_TIME = "seg_time";
	public static final String TASK_VSIZE = "vsize";
	public static final String TASK_VIDEO_DOMAIN = "video_domain";
	public static final String TASK_VIDEO_PATH = "video_path";
	public static final String TASK_DISPATCH_STATUS = "dispatch_status";
	public static final String TASK_DISPATCH_SRC_IP = "dispatch_src_ip";
	public static final String TASK_DISPATCH_START_TIME = "dispatch_start_time";
	public static final String TASK_DISPATCH_FINISH_TIME = "dispatch_finish_time";
	public static final String TASK_REPORT_STATUS = "report_status";
	public static final String TASK_REPORT_START_TIME = "report_start_time";
	public static final String TASK_REPORT_FINISH_TIME = "report_finish_time";
	public static final String TASK_CHECK_STATUS = "check_status";
	public static final String TASK_CHECK_FAIL_REASON = "check_fail_reason";
	public static final String TASK_WAP_ARGS = "wap_args";
	public static final String TASK_COVER_LOGO_AREA = "cover_logo_area";
	public static final String TASK_AUDIT_RESULT = "audit_result";

	// macro
	public static final Integer TASK_ID_TYPE = 1;
	public static final Integer PFID_TYPE = 2;
	public static final Long DELAY_TIME = 60L * 1000;
	
	public static final Integer BASE_PRIORITY = 9;
	public static final Integer STATUS_UNKNOWN = 1;
	public static final Integer STATUS_AUDIT = 2;
	public static final Integer STATUS_COMPLETE = 3;
	public static final Integer STATUS_FAIL = 4;

	// Error states
	public static final Integer STATS_ERR = 1;
	public static final Integer STATS_OK = 2;
	public static final Integer STATS_FILE_INVALID = 3;
	public static final Integer STATS_NOT_EXIST_DOMAIN = 4;
	public static final Integer STATS_NOT_EXIST_DISKID = 5;
	public static final Integer STATS_DOMAIN_NOT_EQ_PATH = 6;
	public static final Integer STATS_DOMAIN_PATH_EQ_NULL = 7;
	
	// Report states
	public static final Integer REPORT_RUNING_AUDIT=1;
	public static final Integer REPORT_AUDIT = 2;
	public static final Integer REPORT_AUDIT_REPORT = 3;
	public static final Integer REPORT_RUNING_COMPLETE=4;
	public static final Integer REPORT_COMPLETE = 5;
	public static final Integer REPORT_PFID_ERROR = 6;
	public static final Integer REPORT_ERROR = -1;
	
	public static final Integer DEF_STATUS = 1;
	public static final Integer ERR_STATUS = 2;
	public static final Integer OK_STATUS = 3;
	
	public static final Long TASK_ID_TIME = 60L * 1000;
	public static final Long PFID_TIME = 30L * 1000;
	
	public static final Set<String> HOT_CFROM = Sets.newHashSet(
			"live", 
			"vku", 
			"bj_pb_upload");
	
	// Common status
	public static final Integer COMMON_GROUP_IN_USE = 1;
	public static final Integer COMMON_GROUP_NOT_USE = 0;
	public static final Integer COMMON_GROUP_PROXY = 0;
	
	public static final Integer COMMON_NODE_UNACTIVE = 0;
	public static final Integer COMMON_NODE_ACTIVE = 1;
	public static final Integer COMMON_NODE_DOWN = 2;
	
	public static final Integer COMMON_NODE_IN_USE = 1;
	public static final Integer COMMON_NODE_NOT_USE = 0;
	
	public static final Integer COMMON_NODE_NEED_DISPATCH = 1;
	
	public static final Integer COMMON_SVR_DOWN = 0;
	public static final Integer COMMON_SVR_ACTIVE = 1;
	
	public static final Integer COMMON_SVR_BAD = 0;
	public static final Integer COMMON_SVR_OK = 1;
	
	public static final Integer COMMON_SVR_CDN = 0;
	public static final Integer COMMON_SVR_TRANSCODE = 1;

	public static final Integer COMMON_SVR_IN_USE = 1;
	public static final Integer COMMON_SVR_NOT_USE = 0;
	
	public static final Integer COMMON_DISK_BAD = 0;
	public static final Integer COMMON_DISK_OK = 1;
	
	public static final Integer COMMON_DISK_IN_USE = 1;
	public static final Integer COMMON_DISK_NOT_USE = 0;
	
	public static final Integer COMMON_GLOBAL_MOD = 10000;
	
	public static final Integer COMMON_NORMAL_NODE_TYPE = 1;
	public static final Integer COMMON_PROXY_NODE_TYPE = 0;
	
	public static final Integer COMMON_GROUP_COLD = 1;
	public static final Integer COMMON_NODE_COLD = 2;
	public static final Integer COMMON_NODE_HOT = 3;
	public static final Integer COMMON_STORE = 4;
	
	public static final Integer COMMON_NORMAL_DO_TASK = 1;
	public static final Integer COMMON_FULL_DO_TASK = 2;
	
	// Global Settings
	public static final Long MIN_DISK_SIZE = 5L;
	public static final Integer LEFT_SIZE_INIT_TYPE = 1;
	public static final Integer SQUARE_INIT_TYPE = 2;
	public static final Integer DEFAULT_MAX_EXTEND = 1000;
	
	// SYN Task
	public static final Integer SVR_TYPE_INIT_SRC = 1;
	public static final Integer SVR_TYPE_CDN_SRC = 2;
	public static final Integer OPT_SYN = 1;
	public static final Integer OPT_DEL = 2;
	public static final Integer OPT_CHECK = 3;
	public static final Integer OPT_DISK_CHECK = 4;
	public static final Integer OPT_FULL_DISK = 100;
	public static final Integer TASK_UNKNOWN = 0;
	public static final Integer TASK_INIT_DEST = 1;
	public static final Integer TASK_QUEUE = 4;
	public static final Integer TASK_WAITING = 5;
	public static final Integer TASK_COMPLETE = 6;
	
	// Dispatch status
	public static final Integer DISPATCH_STATUS_OK = 3;
	public static final Integer DISPATHC_STATUS_FAILED = 4;
	
	// Task dispatch status
	public static final Integer TASK_DSP_UNKNOWN = -1;
	// TODO: continue
	//
	//
	//

	private Constrants() {}
}
