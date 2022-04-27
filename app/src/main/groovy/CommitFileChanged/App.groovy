/*
 * This Groovy source file was generated by the Gradle 'init' task.
 */
package com.CommitFileChanged
import project.Project
import dataCollectors.MethodSignatureMatcher
import project.MergeCommit
import util.FileManager
import util.ProcessRunner
import util.SMATConfig

class App {
    static final String csv_name = "/analysis_info.csv";
    Project project
    MergeCommit merge_scenario
    String working_dir

     Map<String, List<String>> getModifiedClassMethodsList( ) {
        IdentifyMutualChanges imc = new IdentifyMutualChanges();
        Map<String, List<String>> ModifiedList = imc.containsMutuallyModifiedMethods(this.project, this.merge_scenario)
        return ModifiedList
    }

    void def_mergeScenario(args){
        String head = args[1]
        String[] parents = [args[2], args[3]]
        String ancestor = args[4]
        this.merge_scenario = new MergeCommit(head, parents, ancestor)
        if(this.merge_scenario.getParentsSHA().contains(this.merge_scenario.getAncestorSHA()))
            System.exit(1)

    }

    List<String> generateAnalysis(String[] args){
        this.working_dir = args[5]
        def outp;
        project = new Project(working_dir)
        def_mergeScenario(args)
        String dependencies_path = args[6]
        String maven_home = args[7]
        String java_home = args[8]
        SMATConfig.createConfig(dependencies_path, java_home, maven_home)
        System.setProperty('dependencies.path', dependencies_path )
        def csv = new File(dependencies_path + csv_name)
        def jarsPath = this.working_dir + "/../GeneratedJars/" + this.project.getName() + "/"
        FileManager.delete(csv)
        Map<String, List<String>> modifiedClassMethods = getModifiedClassMethodsList()
        if(modifiedClassMethods.isEmpty())
            System.exit(1)
        for (class_name in modifiedClassMethods.keySet()) {
            for (method in modifiedClassMethods.get(class_name)) {
                String method_name = ((String)method).split('\\(')[0]
                String[] class_path = class_name.split('/')
                String class_name_p = class_path[class_path.length -1];
                Process osean = ProcessRunner.runProcess('.', 'java', '-jar', dependencies_path+'/Osean.jar', working_dir , class_name_p, method_name, this.project.getName(), 'true', 'true', '60', this.merge_scenario.getSHA(), this.merge_scenario.getLeftSHA(), this.merge_scenario.getRightSHA(), this.merge_scenario.getAncestorSHA() )
                BufferedReader reader = new BufferedReader(new InputStreamReader(osean.getInputStream()))
                println reader.readLines()

                String cleaned_class_name = class_name_p.replace('.java', '')
                String f_package = (FileManager.getFilePackageName(this.working_dir + "/" + class_name)) + cleaned_class_name
                //FileManager.changeJarsName(jarsPath, this.merge_scenario, method_name)
                def signature_method_name = new MethodSignatureMatcher().findMethodSignature(jarsPath + this.merge_scenario.getSHA() + '-' + method_name + ".jar", f_package, method)


                String csvLine =this.project.getName() + "," + "true" + "," + this.merge_scenario.getSHA() + "," + this.merge_scenario.getLeftSHA() + "," + this.merge_scenario.getRightSHA() +
                "," +  this.merge_scenario.getAncestorSHA() + "," + f_package + "," + signature_method_name + ","  + ","  + "," +
                jarsPath + this.merge_scenario.getAncestorSHA()  + '-' + method_name + ".jar" + "," +
                jarsPath + this.merge_scenario.getLeftSHA() + '-' + method_name + ".jar" + "," +
                jarsPath + this.merge_scenario.getRightSHA() + '-' + method_name + ".jar" + "," +
                jarsPath + this.merge_scenario.getSHA() + '-' + method_name + ".jar" + "," +
                "transformed"
                csv.append(csvLine + "\n")
            }
        }

        return outp
    }


    static void main(String[] args) {
        if(args[0] == 'Analysis')
            println new App().generateAnalysis(args)
        else
            System.exit(127)
    }
}
