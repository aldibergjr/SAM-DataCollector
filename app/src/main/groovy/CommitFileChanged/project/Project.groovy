package project

import util.ProcessRunner

import java.util.regex.Pattern
import java.util.regex.Matcher

class Project {
    private String name
    private String path
    private String url
    private boolean remote
    static private Pattern REMOTE_REPO_PATTERN = Pattern.compile("((http|https):\\/\\/)?.+.com\\/.+\\/.+")

 Project(String name, String path) {
        this.name = name
        this.path = path
        
        this.remote = checkIfPathIsUrl(path)
        if (this.remote) {
            this.url = this.path
        } else {
            this.url = null
        }
    }

    Project(String path) {
        this.path = path

        this.remote = checkIfPathIsUrl(path)   
    
        this.name = this.getOwnerAndName()[1] 
    }
    
    private checkIfPathIsUrl (String path) {
        Matcher matcher = REMOTE_REPO_PATTERN.matcher(path)
        return matcher.find()
    }

    String getRemoteUrl() {
        if (remote && checkIfPathIsUrl(this.path)) {
            return this.path
        } else if (remote) {
            Process gitProcess = ProcessRunner.runProcess(this.path, "git", "config", "--get", "remote.origin.url")
            String gitProcessText = gitProcess.getText()

            return gitProcessText.trim()
        }
        return null
    }

     String[] getOwnerAndName() {
        String remoteUrl = this.getRemoteUrl()
        if (remoteUrl != null) {
            String[] splitPath = this.remoteUrl.split("/");
            String projectName = splitPath[splitPath.length - 1]
            String projectOwner = splitPath[splitPath.length - 2]

            return [projectOwner, projectName]
        } else {
            String[] splitPath = this.path.split("/")
            String projectName = splitPath[splitPath.length - 1]

            return ["local", projectName]
        }
    }

    String getName() {
        return name
    }

    String getPath() {
        return path
    }

}