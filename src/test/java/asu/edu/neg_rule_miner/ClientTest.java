package asu.edu.neg_rule_miner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.ext.com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import asu.edu.neg_rule_miner.model.horn_rule.HornRule;
import asu.edu.neg_rule_miner.rule_generator.DynamicPruningRuleDiscovery;
import au.com.bytecode.opencsv.CSVReader;

/**
 * Unit test for simple App.
 */
public class ClientTest 
{
  private final DynamicPruningRuleDiscovery rudik = new DynamicPruningRuleDiscovery();
  private final static Logger LOGGER = LoggerFactory.getLogger(ClientTest.class.getName());
  private static final String export_path_all = "/home/apradhan/sampled_rudik_rules/";
  private static final String export_path = "/home/apradhan/sampled_rudik_rules/";
  private static final String predicate = "spouse";
  private static final String data_size = "5k";
  private static final String input_path = "/home/apradhan/proj/fact_check_kb/dataset/"+predicate+"/input/";
  private static final String FILENAME_NEG_COMPACT = input_path+predicate+"_neg_"+data_size+".txt";
  private static final String FILENAME_POS_COMPACT = input_path+predicate+"_pos_"+data_size+".txt";
  private static final String FILENAME_NEG_ALL = export_path_all+predicate+"_neg_all_"+data_size+".txt";
  private static final String FILENAME_POS_ALL = export_path_all+predicate+"_pos_all_1000.txt";
   
  
  BufferedWriter bw = null;
  FileWriter fw = null;
  
  protected Set<Pair<String,String>> getExamples(String csvFile) {
	  System.out.println(csvFile);
      final Set<Pair<String,String>> examples = Sets.newHashSet();
      CSVReader reader = null;
      
      try {
    	  reader = new CSVReader(new FileReader(csvFile));
          String[] line;
          
          
          while ((line = reader.readNext()) != null) {
//        	  System.out.println(line);
//        	  System.out.println(line[0] +" , " + line[1]);
              final Pair<String,String> example = Pair.of("http://dbpedia.org/resource/"+line[0], "http://dbpedia.org/resource/"+line[1]);
  	    		examples.add(example);
          }

      } catch (IOException e) {
          e.printStackTrace();
      } 
	  return examples;

  }
  
  protected List<HornRule> executeRudikNegativeRules(final Set<String> relationNames, final String subjectType, final String objectType, final String rel, final String size){
    final Instant startTime = Instant.now();
    
//    get positive and negative examples
//    final Set<Pair<String,String>> negativeExamples = rudik.
//        generateNegativeExamples(relationNames, subjectType, objectType, false, false);
//    final Set<Pair<String,String>> positiveExamples = rudik.
//        generatePositiveExamples(relationNames, subjectType, objectType);
    
    final String path = "/home/apradhan/proj/fact_check_kb/dataset/"+rel;
    final String output_path = path+"/rules/rudik/"+rel+"_neg_"+size+".csv";
    
    String csvFile_positive = path+"/input/positive_examples_"+size+".csv";
    String csvFile_negative = path+"/input/negative_examples_"+size+".csv";
    
    final Set<Pair<String,String>> negativeExamples = getExamples(csvFile_negative);
    final Set<Pair<String,String>> positiveExamples = getExamples(csvFile_positive);

    //compute outputs
    final List<HornRule> outputRules = rudik.
        discoverNegativeHornRules(negativeExamples, positiveExamples, relationNames, subjectType, objectType);
    
    final Instant endTime = Instant.now();
    LOGGER.info("----------------------------COMPUTATION ENDED----------------------------");
    LOGGER.info("Relation: {}",(output_path));
    LOGGER.info("Final computation time: {} seconds.",(endTime.toEpochMilli()-startTime.toEpochMilli())/1000.);
    LOGGER.info("----------------------------Final output rules----------------------------");
    for(final HornRule oneRule:outputRules){
      LOGGER.info("{}",oneRule);
      try{
    	  fw = new FileWriter(output_path, true);
    	  bw = new BufferedWriter(fw);
    	  bw.write(oneRule.toString()+"\n");
      }catch (IOException e){
    	  e.printStackTrace();
      }finally{
    	  if (bw != null) try {
    		    bw.close();
    		 } catch (IOException ioe2) {
    		    // just ignore it
    		 }
      }
    }
    return outputRules;
  }
  
  protected List<HornRule> executeRudikPositiveRules(final Set<String> relationNames, final String subjectType, final String objectType, final String rel, final String size){
	    final Instant startTime = Instant.now();	    
	    //get positive and negative examples
//	    final Set<Pair<String,String>> negativeExamples = rudik.
//	        generateNegativeExamples(relationNames, subjectType, objectType, false, false);
//	    final Set<Pair<String,String>> positiveExamples = rudik.
//	        generatePositiveExamples(relationNames, subjectType, objectType);
	    
	    final String path = "/home/apradhan/proj/fact_check_kb/dataset/"+rel;
	    final String output_path = path+"/rules/rudik/"+rel+"_pos_"+size+".csv";
	    
	    
	    String csvFile_positive = path+"/input/positive_examples_"+size+".csv";
	    String csvFile_negative = path+"/input/negative_examples_"+size+".csv";
	    final Set<Pair<String,String>> negativeExamples = getExamples(csvFile_negative);
	    final Set<Pair<String,String>> positiveExamples = getExamples(csvFile_positive);

	    
	    final List<HornRule> outputRules = rudik.discoverPositiveHornRules(negativeExamples, positiveExamples, relationNames, subjectType, objectType);
	    //compute outputs
	    	    
	    final Instant endTime = Instant.now();
	    LOGGER.info("----------------------------COMPUTATION ENDED----------------------------");
	    LOGGER.info("Relation: {}",(output_path));
	    LOGGER.info("Final computation time: {} seconds.",(endTime.toEpochMilli()-startTime.toEpochMilli())/1000.);
	    LOGGER.info("----------------------------Final output rules----------------------------");
	    for(final HornRule oneRule:outputRules){
	      LOGGER.info("{}",oneRule);
	      try{
	    	  fw = new FileWriter(output_path, true);
	    	  bw = new BufferedWriter(fw);
	    	  bw.write(oneRule.toString()+"\n");
	      }catch (IOException e){
	    	  e.printStackTrace();
	      }finally{
	    	  if (bw != null) try {
	    		    bw.close();
	    		 } catch (IOException ioe2) {
	    		    // just ignore it
	    		 }
	      }
	    }
	    return outputRules;
	  }
  
  protected Map<HornRule,Double> executeRudikAllNegativeRules(final Set<String> relationNames, final String subjectType, final String objectType, final int maxRulesNumber,final String rel, final String size){
    final Instant startTime = Instant.now();
    
//    get positive and negative examples
//    final Set<Pair<String,String>> negativeExamples = rudik.
//        generateNegativeExamples(relationNames, subjectType, objectType, false, false);
//    final Set<Pair<String,String>> positiveExamples = rudik.
//        generatePositiveExamples(relationNames, subjectType, objectType);
    
    final String path = "/home/apradhan/proj/fact_check_kb/dataset/"+rel;
    final String output_path = path+"/rules/rudik/"+rel+"_neg_"+size+"_"+maxRulesNumber+".csv";
    
    
    String csvFile_positive = path+"/input/positive_examples_"+size+".csv";
    String csvFile_negative = path+"/input/negative_examples_"+size+".csv";
    final Set<Pair<String,String>> negativeExamples = getExamples(csvFile_negative);
    final Set<Pair<String,String>> positiveExamples = getExamples(csvFile_positive);

    //compute outputs
    final Map<HornRule,Double> outputRules = rudik.
        discoverAllNegativeHornRules(negativeExamples, positiveExamples, relationNames, subjectType, objectType,maxRulesNumber);
    
    final Instant endTime = Instant.now();
    LOGGER.info("----------------------------COMPUTATION ENDED----------------------------");
    LOGGER.info("Final computation time: {} seconds.",(endTime.toEpochMilli()-startTime.toEpochMilli())/1000.);
    LOGGER.info("----------------------------Final output rules----------------------------");
    for(final HornRule oneRule:outputRules.keySet()){
      LOGGER.info("Rule:{}\tScore:{}",oneRule,outputRules.get(oneRule));
      try{
    	  fw = new FileWriter(output_path, true);
    	  bw = new BufferedWriter(fw);
    	  bw.write(oneRule.toString()+","+String.format("%.5f",outputRules.get(oneRule))+"\n");
      }catch (IOException e){
    	  e.printStackTrace();
      }finally{
    	  if (bw != null) try {
    		    bw.close();
    		 } catch (IOException ioe2) {
    		    // just ignore it
    		 }
      }
    }
    return outputRules;
  }
  
  protected Map<HornRule,Double> executeRudikAllPositiveRules(final Set<String> relationNames, final String subjectType, final String objectType, final int maxRulesNumber,final String rel, final String size ){
	    final Instant startTime = Instant.now();   
	    //get positive and negative examples
//	    final Set<Pair<String,String>> negativeExamples = rudik.
//	        generateNegativeExamples(relationNames, subjectType, objectType, false, false);
//	    final Set<Pair<String,String>> positiveExamples = rudik.
//	        generatePositiveExamples(relationNames, subjectType, objectType);
	    
	    final String path = "/home/apradhan/proj/fact_check_kb/dataset/"+rel;
	    final String output_path = path+"/rules/rudik/"+rel+"_pos_"+size+"_"+maxRulesNumber+".csv";
	    
	    
	    String csvFile_positive = path+"/input/positive_examples_"+size+".csv";
	    String csvFile_negative = path+"/input/negative_examples_"+size+".csv";
	    final Set<Pair<String,String>> negativeExamples = getExamples(csvFile_negative);
	    final Set<Pair<String,String>> positiveExamples = getExamples(csvFile_positive);
	    
	    final Map<HornRule, Double> outputRules = rudik.discoverAllPositiveHornRules(negativeExamples, positiveExamples, relationNames, subjectType, objectType, maxRulesNumber); 
	    
	    final Instant endTime = Instant.now();
	    LOGGER.info("----------------------------COMPUTATION ENDED----------------------------");
	    LOGGER.info("Final computation time: {} seconds.",(endTime.toEpochMilli()-startTime.toEpochMilli())/1000.);
	    LOGGER.info("----------------------------Final output rules----------------------------");
	    for(final HornRule oneRule:outputRules.keySet()){
	      LOGGER.info("Rule:{}\tScore:{}",oneRule,outputRules.get(oneRule));
	      try{
	    	  fw = new FileWriter(output_path, true);
	    	  bw = new BufferedWriter(fw);
	    	  bw.write(oneRule.toString()+","+String.format("%.5f",outputRules.get(oneRule))+"\n");
	      }catch (IOException e){
	    	  e.printStackTrace();
	      }finally{
	    	  if (bw != null) try {
	    		    bw.close();
	    		 } catch (IOException ioe2) {
	    		    // just ignore it
	    		 }
	      }
	      }
	    return outputRules;
	  }
  
}
