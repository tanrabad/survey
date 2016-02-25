package th.or.nectec.tanrabad.survey.job;


public abstract class UploadJob implements Job {
    protected int successCount = 0;
    protected int ioExceptionCount = 0;
    protected int restServiceExceptionCount = 0;

    public boolean isUploadData() {
        return getSuccessCount() > 0 || getFailCount() > 0;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getFailCount() {
        return getIoExceptionCount() + getRestServiceExceptionCount();
    }

    public int getIoExceptionCount() {
        return ioExceptionCount;
    }

    public int getRestServiceExceptionCount() {
        return restServiceExceptionCount;
    }

    public boolean isUploadCompletelySuccess() {
        return getFailCount() == 0 && getSuccessCount() > 0;
    }

    public boolean isUploadCompletelyFail() {
        return getFailCount() > 0 && getSuccessCount() == 0;
    }
}
