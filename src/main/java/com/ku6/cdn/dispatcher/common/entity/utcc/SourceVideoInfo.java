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
@Table(name = SOURCE_VIDEO_INFO)
public class SourceVideoInfo {

	private Long id;
	private Long vid;
	private Long sid;
	private Long uid;
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
	private Long uploadSpeed;
	private Integer priority;
	private String srcDomain;
	private String srcPath;
	private Long duration;
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
	private Long fileSize;
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
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = SVI_ID, length = 20, nullable = false, unique = true)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = SVI_VID, length = 11)
	public Long getVid() {
		return vid;
	}
	
	public void setVid(Long vid) {
		this.vid = vid;
	}
	
	@Column(name = SVI_SID, length = 20)
	public Long getSid() {
		return sid;
	}
	
	public void setSid(Long sid) {
		this.sid = sid;
	}
	
	@Column(name = SVI_UID, length = 11)
	public Long getUid() {
		return uid;
	}
	
	public void setUid(Long uid) {
		this.uid = uid;
	}
	
	@Column(name = SVI_STATUS, length = 6)
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = SVI_PLAY_STATUS, length = 4)
	public Integer getPlayStatus() {
		return playStatus;
	}
	
	public void setPlayStatus(Integer playStatus) {
		this.playStatus = playStatus;
	}
	
	@Column(name = SVI_CFROM)
	public String getCfrom() {
		return cfrom;
	}
	
	public void setCfrom(String cfrom) {
		this.cfrom = cfrom;
	}
	
	@Column(name = SVI_CLIENT_IP)
	public String getClientIp() {
		return clientIp;
	}
	
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	
	@Column(name = SVI_TITLE)
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = SVI_UPLOAD_SERVER, nullable = false)
	public String getUploadServer() {
		return uploadServer;
	}
	
	public void setUploadServer(String uploadServer) {
		this.uploadServer = uploadServer;
	}
	
	@Column(name = SVI_UPLOAD_CREATE_TIME)
	public Date getUploadCreateTime() {
		return uploadCreateTime;
	}
	
	public void setUploadCreateTime(Date uploadCreateTime) {
		this.uploadCreateTime = uploadCreateTime;
	}
	
	@Column(name = SVI_UPLOAD_START_TIME)
	public Date getUploadStartTime() {
		return uploadStartTime;
	}
	
	public void setUploadStartTime(Date uploadStartTime) {
		this.uploadStartTime = uploadStartTime;
	}
	
	@Column(name = SVI_UPLOAD_FINISH_TIME)
	public Date getUploadFinishTime() {
		return uploadFinishTime;
	}
	
	public void setUploadFinishTime(Date uploadFinishTime) {
		this.uploadFinishTime = uploadFinishTime;
	}
	
	@Column(name = SVI_UPLOAD_STATUS, length = 6)
	public Integer getUploadStatus() {
		return uploadStatus;
	}
	
	public void setUploadStatus(Integer uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
	
	@Column(name = SVI_UPLOAD_SPEED, length = 11)
	public Long getUploadSpeed() {
		return uploadSpeed;
	}
	
	public void setUploadSpeed(Long uploadSpeed) {
		this.uploadSpeed = uploadSpeed;
	}
	
	@Column(name = SVI_PRIORITY, length = 6)
	public Integer getPriority() {
		return priority;
	}
	
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	@Column(name = SVI_SRC_DOMAIN)
	public String getSrcDomain() {
		return srcDomain;
	}
	
	public void setSrcDomain(String srcDomain) {
		this.srcDomain = srcDomain;
	}
	
	@Column(name = SVI_SRC_PATH)
	public String getSrcPath() {
		return srcPath;
	}
	
	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}
	
	@Column(name = SVI_DURATION, length = 11)
	public Long getDuration() {
		return duration;
	}
	
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	
	@Column(name = SVI_FILE_FORMAT)
	public String getFileFormat() {
		return fileFormat;
	}
	
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	
	@Column(name = SVI_VIDEO_FORMAT)
	public String getVideoFormat() {
		return videoFormat;
	}
	
	public void setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
	}
	
	@Column(name = SVI_VIDEO_BITRATE, length = 6)
	public Integer getVideoBitrate() {
		return videoBitrate;
	}
	
	public void setVideoBitrate(Integer videoBitrate) {
		this.videoBitrate = videoBitrate;
	}
	
	@Column(name = SVI_WIDTH, length = 6)
	public Integer getWidth() {
		return width;
	}
	
	public void setWidth(Integer width) {
		this.width = width;
	}
	
	@Column(name = SVI_HEIGHT, length = 6)
	public Integer getHeight() {
		return height;
	}
	
	public void setHeight(Integer height) {
		this.height = height;
	}
	
	@Column(name = SVI_PIXEL_SCALE)
	public String getPixelScale() {
		return pixelScale;
	}
	
	public void setPixelScale(String pixelScale) {
		this.pixelScale = pixelScale;
	}
	
	@Column(name = SVI_DISPLAY_SCALE)
	public String getDisplayScale() {
		return displayScale;
	}
	
	public void setDisplayScale(String displayScale) {
		this.displayScale = displayScale;
	}
	
	@Column(name = SVI_TRANSCODE_PREFER)
	public String getTranscodePrefer() {
		return transcodePrefer;
	}
	
	public void setTranscodePrefer(String transcodePrefer) {
		this.transcodePrefer = transcodePrefer;
	}
	
	@Column(name = SVI_FPS, length = 6)
	public Integer getFps() {
		return fps;
	}
	
	public void setFps(Integer fps) {
		this.fps = fps;
	}
	
	@Column(name = SVI_MD5)
	public String getMd5() {
		return md5;
	}
	
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	@Column(name = SVI_FILE_SIZE, length = 20)
	public Long getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
	@Column(name = SVI_ALL_PREF)
	public String getAllPref() {
		return allPref;
	}
	
	public void setAllPref(String allPref) {
		this.allPref = allPref;
	}
	
	@Column(name = SVI_AUDIO_STREAM_COUNT, length = 6)
	public Integer getAudioStreamCount() {
		return audioStreamCount;
	}
	
	public void setAudioStreamCount(Integer audioStreamCount) {
		this.audioStreamCount = audioStreamCount;
	}
	
	@Column(name = SVI_AUDIO_STREAM)
	public String getAudioStream() {
		return audioStream;
	}
	
	public void setAudioStream(String audioStream) {
		this.audioStream = audioStream;
	}
	
	@Column(name = SVI_WATERMARK, length = 6)
	public Integer getWatermark() {
		return watermark;
	}
	
	public void setWatermark(Integer watermark) {
		this.watermark = watermark;
	}
	
	@Column(name = SVI_TRANSCODE_TASK_COUNT, length = 6)
	public Integer getTranscodeTaskCount() {
		return transcodeTaskCount;
	}
	
	public void setTranscodeTaskCount(Integer transcodeTaskCount) {
		this.transcodeTaskCount = transcodeTaskCount;
	}
	
	@Column(name = SVI_REPORT_ERR, length = 6)
	public Integer getReportErr() {
		return reportErr;
	}
	
	public void setReportErr(Integer reportErr) {
		this.reportErr = reportErr;
	}
	
	@Column(name = SVI_EXTRA_ARGS)
	public String getExtraArgs() {
		return extraArgs;
	}
	
	public void setExtraArgs(String extraArgs) {
		this.extraArgs = extraArgs;
	}
	
	@Column(name = SVI_VIDEO_FROM_SITE)
	public String getVideoFromSite() {
		return videoFromSite;
	}
	
	public void setVideoFromSite(String videoFromSite) {
		this.videoFromSite = videoFromSite;
	}
	
	@Column(name = SVI_WAP_ARGS)
	public String getWapArgs() {
		return wapArgs;
	}
	
	public void setWapArgs(String wapArgs) {
		this.wapArgs = wapArgs;
	}
	
	@Column(name = SVI_COVER_LOGO_AREA)
	public String getCoverLogoArea() {
		return coverLogoArea;
	}
	
	public void setCoverLogoArea(String coverLogoArea) {
		this.coverLogoArea = coverLogoArea;
	}
	
	@Column(name = SVI_NEED_AUDIT, length = 4)
	public Integer getNeedAudit() {
		return needAudit;
	}
	
	public void setNeedAudit(Integer needAudit) {
		this.needAudit = needAudit;
	}
	
	@Column(name = SVI_AUDIT_RESULT, length = 4)
	public Integer getAuditResult() {
		return auditResult;
	}
	
	public void setAuditResult(Integer auditResult) {
		this.auditResult = auditResult;
	}
	
	@Column(name = SVI_AUDIT_FINISH_TIME)
	public Date getAuditFinishTime() {
		return auditFinishTime;
	}
	
	public void setAuditFinishTime(Date auditFinishTime) {
		this.auditFinishTime = auditFinishTime;
	}
	
	@Column(name = SVI_ACTIVITY_ID)
	public String getActivityId() {
		return activityId;
	}
	
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

}
