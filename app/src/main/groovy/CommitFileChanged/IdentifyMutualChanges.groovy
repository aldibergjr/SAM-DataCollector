package com.CommitFileChanged

import project.*
import dataCollectors.ModifiedMethod
import util.*
import dataCollectors.ModifiedMethodsHelper

import java.util.stream.Collectors


class IdentifyMutualChanges {

    private modifiedMethodsHelper = new ModifiedMethodsHelper("diffj.jar");

    boolean applyFilter(Project project, MergeCommit mergeCommit) {
        return containsMutuallyModifiedMethods(project, mergeCommit)
    }

    public  Map<String, List<String>> containsMutuallyModifiedMethods(Project project, MergeCommit mergeCommit) {
        if (mergeCommit.getAncestorSHA() == null) {
            /**
            * Some merge scenarios don't return an valid ancestor SHA this check prevents
            * unexpected crashes
            */
            return false;
        }

        Set<String> leftModifiedFiles = FileManager.getModifiedFiles(project, mergeCommit.getLeftSHA(), mergeCommit.getAncestorSHA())
        Set<String> rightModifiedFiles = FileManager.getModifiedFiles(project, mergeCommit.getRightSHA(), mergeCommit.getAncestorSHA())
        Set<String> mutuallyModifiedFiles = new HashSet<String>(leftModifiedFiles)
        mutuallyModifiedFiles.retainAll(rightModifiedFiles)
        Map<String, List<String>> infoModified = new HashMap<>();;
        for(file in mutuallyModifiedFiles) {
            Set<String> leftModifiedAttributesAndMethods = getModifiedAttributesAndMethods(project, file, mergeCommit.getAncestorSHA(), mergeCommit.getLeftSHA())
            Set<String> rightModifiedAttributesAndMethods = getModifiedAttributesAndMethods(project, file, mergeCommit.getAncestorSHA(), mergeCommit.getRightSHA())
            leftModifiedAttributesAndMethods.retainAll(rightModifiedAttributesAndMethods) // Intersection.
            infoModified.put(file, leftModifiedAttributesAndMethods)
        }

        return infoModified
    }

    private Set<String> getModifiedAttributesAndMethods(Project project, String filePath, String ancestorSHA, String targetSHA) {
        return modifiedMethodsHelper.getModifiedMethods(project, filePath, ancestorSHA, targetSHA)
                .stream()
                .map{method -> method.getSignature()}
                .collect(Collectors.toSet())
    }

}