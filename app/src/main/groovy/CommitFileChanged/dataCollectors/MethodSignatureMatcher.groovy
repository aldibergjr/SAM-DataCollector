package dataCollectors

import util.ProcessRunner

public class MethodSignatureMatcher{
    public String findMethodSignature(String jar_path, String class_path, String method_matcher){
        Process javap = ProcessRunner.runProcess('.', 'javap', '-classpath', jar_path, class_path)
        BufferedReader reader = new BufferedReader(new InputStreamReader(javap.getInputStream()))

        List<String> method_list = reader.readLines()
        String method_name = method_matcher.split('\\(')[0]
        List<String> method_params = method_matcher.split('\\(')[1].split(',')

        def matched_methods = method_list.findAll { it.contains(method_name) }
        println matched_methods
        matched_methods = matched_methods.collect { method -> return method.trim().split(' ')[2].replace(';','') }
        // IMPLEMENT FOR OVERLOAD CASES USING METHOD PARAMS\

        return matched_methods[0]
    }
}