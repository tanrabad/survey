/*
 * Copyright (c) 2016 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.or.nectec.tanrabad.survey.job;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsJobRunner implements JobRunner {

    List<Job> jobList = new ArrayList<>();
    boolean running = false;
    int jobFinishCount = 0;
    int jobErrorCount = 0;

    @Override
    public JobRunner addJob(Job job) {
        jobList.add(job);
        return this;
    }

    @Override
    public void start() {
        running = true;
        jobErrorCount = 0;
        jobFinishCount = 0;
        new AsyncJob().execute(jobList.toArray(new Job[jobList.size()]));
    }

    @Override
    public int totalJobs() {
        return jobList.size();
    }

    @Override
    public int finishedJobs() {
        return jobFinishCount;
    }

    @Override
    public int errorJobs() {
        return jobErrorCount;
    }

    protected void onJobError(Job errorJob, Exception exception) {
        jobErrorCount++;
    }

    protected void onJobDone(Job job) {
        jobFinishCount++;
    }

    protected abstract void onJobStart(Job startingJob);

    protected abstract void onRunFinish();

    private enum JobStatus {
        START, DONE, ERROR
    }

    private class AsyncJob extends AsyncTask<Job, Object, Void> {

        @Override
        protected Void doInBackground(Job... jobs) {
            for (Job job : jobs) {
                publishProgress(JobStatus.START, job);
                try {
                    job.execute();
                } catch (Exception jEx) {
                    publishProgress(JobStatus.ERROR, job, jEx);
                }
                publishProgress(JobStatus.DONE, job);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onRunFinish();
        }

        @Override
        protected void onProgressUpdate(Object... jobs) {
            super.onProgressUpdate(jobs);
            JobStatus status = (JobStatus) jobs[0];
            Job job = (Job) jobs[1];
            switch (status) {
                case START:
                    onJobStart(job);
                    break;
                case DONE:
                    onJobDone(job);
                    break;
                case ERROR:
                    onJobError(job, (Exception) jobs[2]);
                    break;
            }
        }
    }
}
