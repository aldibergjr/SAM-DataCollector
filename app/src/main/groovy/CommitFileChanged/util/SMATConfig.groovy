package util

class SMATConfig {
    private static final input_csv_name = "/analysis_info.csv";
    public static void createConfig(String dependencies_path, String java_home, String maven_home){
        def config_json = new File(dependencies_path+"/SMAT/nimrod/tests/env-config.json")
        String config_obj = "{"+
                "\"java_home\": \""+java_home+ "\"," +
                "\"maven_home\": \""+maven_home+ "\"," +
                "\"repo_dir\": \"\","+
                "\"projects_folder\": \"\","+
                "\"path_hash_csv\": \""+ dependencies_path + input_csv_name+ "\"," +
                "\"tests_dst\": \"\","+
                "\"path_output_csv\": \"\"" +
                "}"
        config_json.write config_obj
    }
}
