# SAM-DataCollector


## What it is  
SAM-DataCollector is a project that belongs to the [SAM tool](https://github.com/aldibergjr/SemanticMergeTool) and is the first step of its workflow. 
The execution has two main objectives:  
1. Verify if there was a mutual change on the given merge scenario
2. Set everything up for the tests generation and execution by [SMAT](https://github.com/leusonmario/SMAT) if a mutual change is found.   

## Runing the Project

After cloning you will need to generate the jar for executing it later. Inside the project, just run `./gradlew createJar` and it will be generated at `${your_local_repository}/app/build/libs/data-collector-1.0.jar`

### Inputs

The jar require the following list of inputs, in order, to run correctly: 

- Method name (currently only 'Analysis')
- Head commit SHA 
- Parents commit SHA
- Target GIT project directory
- Dependencies folder (Folder with Osean and DiffJ jars)
- Maven home path
- Java home path

The execution of the jar would look something like this:  
`java -jar ${your_local_repository}/app/build/libs/data-collector-1.0.jar Analysis 7bdb36 8shf34 ksjd22 bca6dk /usr/target_local_rep/ usr/sam_dc_dependencies/ usr/java/ usr/maven/`  

## Dependencies  
[DiffJ](https://github.com/jpace/diffj)  
[Osean](https://github.com/leusonmario/OSean.EX)  
