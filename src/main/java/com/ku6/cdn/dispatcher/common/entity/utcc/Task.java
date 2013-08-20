package com.ku6.cdn.dispatcher.common.entity.utcc;

import static com.ku6.cdn.dispatcher.common.entity.utcc.Names.*;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = TASK)
public class Task {
	
	private Integer taskId;
	private Date taskCreateTime;
	private Integer vid;
	private Integer sid;
	private Integer uid;
	private String srcDomain;
	private String srcPath;
	private String uploadServer;
	private String destDomain;
	private Integer priority;
	private Integer videoBitrate;
	private Integer audioBitrate;
	private Integer width;
	private Integer fps;
	private Integer encodeType;
	private String encodePrefer;
	private String extraArgs;
	private String regulation;
	private Integer watermark;
	private String serverPrefer;
	private String transcodeServer;
	private Date transcodeStartTime;
	private Date transcodeFinishTime;
	private Integer transcodeStatus;
	private String transcodeStatusDesc;
	private String picPath;
	private String bigpicPath;
	private Integer segCount;
	private Integer playTime;
	private String segTime;
	private String vsize;
	private String videoDomain;
	private String videoPath;
	private Integer dispatchStatus;
	private String dispatchSrcIp;
	private Date dispatchStartTime;
	private Date dispatchFinishTime;
	private Integer reportStatus;
	private Date reportStartTime;
	private Date reportFinishTime;
	private Integer checkStatus;
	private String checkFailReason;
	private String wapArgs;
	private String coverLogoArea;
	private Integer auditResult;
	
	public Task() {}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = T_TASK_ID, length = 20, nullable = false)
	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	@Column(name = T_TASK_CREATE_TIME, nullable = true, unique = false)
	public Date getTaskCreateTime() {
		return taskCreateTime;
	}

	public void setTaskCreateTime(Date taskCreateTime) {
		this.taskCreateTime = taskCreateTime;
	}

	@Column(name = T_VID, length = 11, nullable = true, unique = false)
	public Integer getVid() {
		return vid;
	}

	public void setVid(Integer vid) {
		this.vid = vid;
	}

	@Column(name = T_SID, length = 20, nullable = true, unique = false)
	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	@Column(name = T_UID, length = 11, nullable = true, unique = false)
	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	@Column(name = T_SRC_DOMAIN, length = 20, nullable = true, unique = false)
	public String getSrcDomain() {
		return srcDomain;
	}

	public void setSrcDomain(String srcDomain) {
		this.srcDomain = srcDomain;
	}

	@Column(name = T_SRC_PATH, length = 1024, nullable = true, unique = false)
	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	@Column(name = T_UPLOAD_SERVER, nullable = true, unique = false)
	public String getUploadServer() {
		return uploadServer;
	}

	public void setUploadServer(String uploadServer) {
		this.uploadServer = uploadServer;
	}

	@Column(name = T_DEST_DOMAIN, length = 8, nullable = true, unique = false)
	public String getDestDomain() {
		return destDomain;
	}

	public void setDestDomain(String destDomain) {
		this.destDomain = destDomain;
	}

	@Column(name = T_PRIORITY, length = 6, nullable = true, unique = false)
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Column(name = T_VIDEO_BITRATE, length = 6, nullable = true, unique = false)
	public Integer getVideoBitrate() {
		return videoBitrate;
	}

	public void setVideoBitrate(Integer videoBitrate) {
		this.videoBitrate = videoBitrate;
	}

	@Column(name = T_AUDIO_BITRATE, length = 6, nullable = true, unique = false)
	public Integer getAudioBitrate() {
		return audioBitrate;
	}

	public void setAudioBitrate(Integer audioBitrate) {
		this.audioBitrate = audioBitrate;
	}

	@Column(name = T_WIDTH, length = 6, nullable = true, unique = false)
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	@Column(name = T_FPS, length = 6, nullable = true, unique = false)
	public Integer getFps() {
		return fps;
	}

	public void setFps(Integer fps) {
		this.fps = fps;
	}

	@Column(name = T_ENCODE_TYPE, length = 6, nullable = true, unique = false)
	public Integer getEncodeType() {
		return encodeType;
	}

	public void setEncodeType(Integer encodeType) {
		this.encodeType = encodeType;
	}

	@Column(name = T_ENCODE_PREFER, nullable = true, unique = false)
	public String getEncodePrefer() {
		return encodePrefer;
	}

	public void setEncodePrefer(String encodePrefer) {
		this.encodePrefer = encodePrefer;
	}

	@Column(name = T_EXTRA_ARGS, nullable = true, unique = false)
	public String getExtraArgs() {
		return extraArgs;
	}

	public void setExtraArgs(String extraArgs) {
		this.extraArgs = extraArgs;
	}

	@Column(name = T_REGULATION, length = 16, nullable = true, unique = false)
	public String getRegulation() {
		return regulation;
	}

	public void setRegulation(String regulation) {
		this.regulation = regulation;
	}

	@Column(name = T_WATERMARK, length = 6, nullable = true, unique = false)
	public Integer getWatermark() {
		return watermark;
	}

	public void setWatermark(Integer watermark) {
		this.watermark = watermark;
	}

	@Column(name = T_SERVER_PREFER, length = 32, nullable = true, unique = false)
	public String getServerPrefer() {
		return serverPrefer;
	}

	public void setServerPrefer(String serverPrefer) {
		this.serverPrefer = serverPrefer;
	}

	@Column(name = T_TRANSCODE_SERVER, length = 64, nullable = true, unique = false)
	public String getTranscodeServer() {
		return transcodeServer;
	}

	public void setTranscodeServer(String transcodeServer) {
		this.transcodeServer = transcodeServer;
	}

	@Column(name = T_TRANSCODE_START_TIME, nullable = true, unique = false)
	public Date getTranscodeStartTime() {
		return transcodeStartTime;
	}

	public void setTranscodeStartTime(Date transcodeStartTime) {
		this.transcodeStartTime = transcodeStartTime;
	}

	@Column(name = T_TRANSCODE_FINISH_TIME, nullable = true, unique = false)
	public Date getTranscodeFinishTime() {
		return transcodeFinishTime;
	}

	public void setTranscodeFinishTime(Date transcodeFinishTime) {
		this.transcodeFinishTime = transcodeFinishTime;
	}

	@Column(name = T_TRANSCODE_STATUS, length = 6, nullable = true, unique = false)
	public Integer getTranscodeStatus() {
		return transcodeStatus;
	}

	public void setTranscodeStatus(Integer transcodeStatus) {
		this.transcodeStatus = transcodeStatus;
	}

	@Column(name = T_TRANSCODE_STATUS_DESC, nullable = true, unique = false)
	public String getTranscodeStatusDesc() {
		return transcodeStatusDesc;
	}

	public void setTranscodeStatusDesc(String transcodeStatusDesc) {
		this.transcodeStatusDesc = transcodeStatusDesc;
	}

	@Column(name = T_PIC_PATH, length = 256, nullable = true, unique = false)
	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	@Column(name = T_BIGPIC_PATH, length = 256, nullable = true, unique = false)
	public String getBigpicPath() {
		return bigpicPath;
	}

	public void setBigpicPath(String bigpicPath) {
		this.bigpicPath = bigpicPath;
	}

	@Column(name = T_SEG_COUNT, length = 5, nullable = true, unique = false)
	public Integer getSegCount() {
		return segCount;
	}

	public void setSegCount(Integer segCount) {
		this.segCount = segCount;
	}

	@Column(name = T_PLAY_TIME, nullable = true, unique = false)
	public Integer getPlayTime() {
		return playTime;
	}

	public void setPlayTime(Integer playTime) {
		this.playTime = playTime;
	}

	@Column(name = T_SEG_TIME, nullable = true, unique = false)
	public String getSegTime() {
		return segTime;
	}

	public void setSegTime(String segTime) {
		this.segTime = segTime;
	}

	@Column(name = T_VSIZE, nullable = true, unique = false)
	public String getVsize() {
		return vsize;
	}

	public void setVsize(String vsize) {
		this.vsize = vsize;
	}

	@Column(name = T_VIDEO_DOMAIN, nullable = true, unique = false)
	public String getVideoDomain() {
		return videoDomain;
	}

	public void setVideoDomain(String videoDomain) {
		this.videoDomain = videoDomain;
	}

	@Column(name = T_VIDEO_PATH, nullable = true, unique = false)
	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	@Column(name = T_DISPATCH_STATUS, length = 6, nullable = true, unique = false)
	public Integer getDispatchStatus() {
		return dispatchStatus;
	}

	public void setDispatchStatus(Integer dispatchStatus) {
		this.dispatchStatus = dispatchStatus;
	}

	@Column(name = T_DISPATCH_SRC_IP, length = 20, nullable = true, unique = false)
	public String getDispatchSrcIp() {
		return dispatchSrcIp;
	}

	public void setDispatchSrcIp(String dispatchSrcIp) {
		this.dispatchSrcIp = dispatchSrcIp;
	}

	@Column(name = T_DISPATCH_START_TIME, nullable = true, unique = false)
	public Date getDispatchStartTime() {
		return dispatchStartTime;
	}

	public void setDispatchStartTime(Date dispatchStartTime) {
		this.dispatchStartTime = dispatchStartTime;
	}

	@Column(name = T_DISPATCH_FINISH_TIME, nullable = true, unique = false)
	public Date getDispatchFinishTime() {
		return dispatchFinishTime;
	}

	public void setDispatchFinishTime(Date dispatchFinishTime) {
		this.dispatchFinishTime = dispatchFinishTime;
	}

	@Column(name = T_REPORT_STATUS, length = 6, nullable = true, unique = false)
	public Integer getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(Integer reportStatus) {
		this.reportStatus = reportStatus;
	}

	@Column(name = T_REPORT_START_TIME, nullable = true, unique = false)
	public Date getReportStartTime() {
		return reportStartTime;
	}

	public void setReportStartTime(Date reportStartTime) {
		this.reportStartTime = reportStartTime;
	}

	@Column(name = T_REPORT_FINISH_TIME, nullable = true, unique = false)
	public Date getReportFinishTime() {
		return reportFinishTime;
	}

	public void setReportFinishTime(Date reportFinishTime) {
		this.reportFinishTime = reportFinishTime;
	}

	@Column(name = T_CHECK_STATUS, length = 6, nullable = true, unique = false)
	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	@Column(name = T_CHECK_FAIL_REASON, length = 96, nullable = true, unique = false)
	public String getCheckFailReason() {
		return checkFailReason;
	}

	public void setCheckFailReason(String checkFailReason) {
		this.checkFailReason = checkFailReason;
	}

	@Column(name = T_WAP_ARGS, nullable = true, unique = false)
	public String getWapArgs() {
		return wapArgs;
	}

	public void setWapArgs(String wapArgs) {
		this.wapArgs = wapArgs;
	}

	@Column(name = T_COVER_LOGO_AREA, length = 128, nullable = true, unique = false)
	public String getCoverLogoArea() {
		return coverLogoArea;
	}

	public void setCoverLogoArea(String coverLogoArea) {
		this.coverLogoArea = coverLogoArea;
	}

	@Column(name = T_AUDIT_RESULT, length = 4, nullable = true, unique = false)
	public Integer getAuditResult() {
		return auditResult;
	}

	public void setAuditResult(Integer auditResult) {
		this.auditResult = auditResult;
	}

}
