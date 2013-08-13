package com.ku6.cdn.dispatcher.common.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "Source_Video_Info")
public class SourceVideoInfo {

	private Integer id;
	private Integer vid;
	private Integer sid;
	private Integer uid;
	private Integer status;
	private Integer playStatus;
	private String cfrom;
	private String clientIp;
	private String title;
	private String uploadServer;
	private Date uploadCreateTime;
	private Date uploadStartTime;
	private Date uploadFinishTime;
	private Integer uploadStatus;
	private Integer uploadSpeed;
	private Integer priority;
	private String srcDomain;
	private String srcPath;
	private Integer duration;
	private String fileFormat;
	private String videoFormat;
	private Integer videoBitrate;
	private Integer width;
	private Integer height;
	private String pixelScale;
	private String displayScale;
	private String transcodePrefer;
	private Integer fps;
	private String md5;
	private Integer fileSize;
	private String allPref;
	private Integer audioStreamCount;
	private String audioStream;
	private Integer watermark;
	private Integer transcodeTaskCount;
	private Integer reportErr;
	private String extraArgs;
	private String videoFromSite;
	private String wapArgs;
	private String coverLogoArea;
	private Integer needAudit;
	private Integer auditResult;
	private Date auditFinishTime;
	private String activityId;
	
	public SourceVideoInfo() {}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getVid() {
		return vid;
	}
	
	public void setVid(Integer vid) {
		this.vid = vid;
	}
	
	public Integer getSid() {
		return sid;
	}
	
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	
	public Integer getUid() {
		return uid;
	}
	
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getPlayStatus() {
		return playStatus;
	}
	
	public void setPlayStatus(Integer playStatus) {
		this.playStatus = playStatus;
	}
	
	public String getCfrom() {
		return cfrom;
	}
	
	public void setCfrom(String cfrom) {
		this.cfrom = cfrom;
	}
	
	public String getClientIp() {
		return clientIp;
	}
	
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getUploadServer() {
		return uploadServer;
	}
	
	public void setUploadServer(String uploadServer) {
		this.uploadServer = uploadServer;
	}
	
	public Date getUploadCreateTime() {
		return uploadCreateTime;
	}
	
	public void setUploadCreateTime(Date uploadCreateTime) {
		this.uploadCreateTime = uploadCreateTime;
	}
	
	public Date getUploadStartTime() {
		return uploadStartTime;
	}
	
	public void setUploadStartTime(Date uploadStartTime) {
		this.uploadStartTime = uploadStartTime;
	}
	
	public Date getUploadFinishTime() {
		return uploadFinishTime;
	}
	
	public void setUploadFinishTime(Date uploadFinishTime) {
		this.uploadFinishTime = uploadFinishTime;
	}
	
	public Integer getUploadStatus() {
		return uploadStatus;
	}
	
	public void setUploadStatus(Integer uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
	
	public Integer getUploadSpeed() {
		return uploadSpeed;
	}
	
	public void setUploadSpeed(Integer uploadSpeed) {
		this.uploadSpeed = uploadSpeed;
	}
	
	public Integer getPriority() {
		return priority;
	}
	
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	public String getSrcDomain() {
		return srcDomain;
	}
	
	public void setSrcDomain(String srcDomain) {
		this.srcDomain = srcDomain;
	}
	
	public String getSrcPath() {
		return srcPath;
	}
	
	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}
	
	public Integer getDuration() {
		return duration;
	}
	
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	public String getFileFormat() {
		return fileFormat;
	}
	
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	
	public String getVideoFormat() {
		return videoFormat;
	}
	
	public void setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
	}
	
	public Integer getVideoBitrate() {
		return videoBitrate;
	}
	
	public void setVideoBitrate(Integer videoBitrate) {
		this.videoBitrate = videoBitrate;
	}
	
	public Integer getWidth() {
		return width;
	}
	
	public void setWidth(Integer width) {
		this.width = width;
	}
	
	public Integer getHeight() {
		return height;
	}
	
	public void setHeight(Integer height) {
		this.height = height;
	}
	
	public String getPixelScale() {
		return pixelScale;
	}
	
	public void setPixelScale(String pixelScale) {
		this.pixelScale = pixelScale;
	}
	
	public String getDisplayScale() {
		return displayScale;
	}
	public void setDisplayScale(String displayScale) {
		this.displayScale = displayScale;
	}
	public String getTranscodePrefer() {
		return transcodePrefer;
	}
	public void setTranscodePrefer(String transcodePrefer) {
		this.transcodePrefer = transcodePrefer;
	}
	
	public Integer getFps() {
		return fps;
	}
	
	public void setFps(Integer fps) {
		this.fps = fps;
	}
	
	public String getMd5() {
		return md5;
	}
	
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	public Integer getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}
	
	public String getAllPref() {
		return allPref;
	}
	
	public void setAllPref(String allPref) {
		this.allPref = allPref;
	}
	
	public Integer getAudioStreamCount() {
		return audioStreamCount;
	}
	
	public void setAudioStreamCount(Integer audioStreamCount) {
		this.audioStreamCount = audioStreamCount;
	}
	
	public String getAudioStream() {
		return audioStream;
	}
	
	public void setAudioStream(String audioStream) {
		this.audioStream = audioStream;
	}
	
	public Integer getWatermark() {
		return watermark;
	}
	
	public void setWatermark(Integer watermark) {
		this.watermark = watermark;
	}
	
	public Integer getTranscodeTaskCount() {
		return transcodeTaskCount;
	}
	
	public void setTranscodeTaskCount(Integer transcodeTaskCount) {
		this.transcodeTaskCount = transcodeTaskCount;
	}
	
	public Integer getReportErr() {
		return reportErr;
	}
	
	public void setReportErr(Integer reportErr) {
		this.reportErr = reportErr;
	}
	
	public String getExtraArgs() {
		return extraArgs;
	}
	
	public void setExtraArgs(String extraArgs) {
		this.extraArgs = extraArgs;
	}
	
	public String getVideoFromSite() {
		return videoFromSite;
	}
	
	public void setVideoFromSite(String videoFromSite) {
		this.videoFromSite = videoFromSite;
	}
	
	public String getWapArgs() {
		return wapArgs;
	}
	
	public void setWapArgs(String wapArgs) {
		this.wapArgs = wapArgs;
	}
	
	public String getCoverLogoArea() {
		return coverLogoArea;
	}
	
	public void setCoverLogoArea(String coverLogoArea) {
		this.coverLogoArea = coverLogoArea;
	}
	
	public Integer getNeedAudit() {
		return needAudit;
	}
	
	public void setNeedAudit(Integer needAudit) {
		this.needAudit = needAudit;
	}
	
	public Integer getAuditResult() {
		return auditResult;
	}
	
	public void setAuditResult(Integer auditResult) {
		this.auditResult = auditResult;
	}
	
	public Date getAuditFinishTime() {
		return auditFinishTime;
	}
	
	public void setAuditFinishTime(Date auditFinishTime) {
		this.auditFinishTime = auditFinishTime;
	}
	
	public String getActivityId() {
		return activityId;
	}
	
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

}
