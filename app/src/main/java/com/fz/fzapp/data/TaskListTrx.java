package com.fz.fzapp.data;

/**
 * Dibuat oleh : ignat
 * Tanggal : 25-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class TaskListTrx
{
  private int ReportID;
  private int lnkUserID;
  private int lnkTaskID;
  private int lnkLocationID;
  private String Notes;
  private int Status;

  public int getReportID() {
    return ReportID;
  }

  public void setReportID(int reportID) {
    ReportID = reportID;
  }

  public int getLnkUserID() {
    return lnkUserID;
  }

  public void setLnkUserID(int lnkUserID) {
    this.lnkUserID = lnkUserID;
  }

  public int getLnkTaskID() {
    return lnkTaskID;
  }

  public void setLnkTaskID(int lnkTaskID) {
    this.lnkTaskID = lnkTaskID;
  }

  public int getLnkLocationID() {
    return lnkLocationID;
  }

  public void setLnkLocationID(int lnkLocationID) {
    this.lnkLocationID = lnkLocationID;
  }

  public String getNotes() {
    return Notes;
  }

  public void setNotes(String notes) {
    Notes = notes;
  }

  public int getStatus() {
    return Status;
  }

  public void setStatus(int status) {
    Status = status;
  }
}
