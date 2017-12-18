package asu.edu.neg_rule_miner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

/**
 * Unit test for simple App.
 */
public class ClientTest 
{
  private final DynamicPruningRuleDiscovery rudik = new DynamicPruningRuleDiscovery();
  private final static Logger LOGGER = LoggerFactory.getLogger(ClientTest.class.getName());
  
  
  protected Set<Pair<String,String>> getExamples(String csvFile) {
      BufferedReader br = null;
      String line = "";
      String cvsSplitBy = ",";
      final Set<Pair<String,String>> examples = Sets.newHashSet();
      
      try {
          br = new BufferedReader(new FileReader(csvFile));
          while ((line = br.readLine()) != null) {
              String[] csvExample = line.split(cvsSplitBy);
              System.out.println(csvExample[0] +" , " + csvExample[2]);
              final Pair<String,String> example = Pair.of("http://dbpedia.org/resource/"+csvExample[0], "http://dbpedia.org/resource/"+csvExample[2]);
  	    		examples.add(example);
          }

      } catch (FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      } finally {
          if (br != null) {
              try {
                  br.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
	  return examples;

  }
  
  protected List<HornRule> executeRudikNegativeRules(final Set<String> relationNames, final String subjectType, final String objectType){
    final Instant startTime = Instant.now();
    
    //get positive and negative examples
    final Set<Pair<String,String>> negativeExamples = rudik.
        generateNegativeExamples(relationNames, subjectType, objectType, false, false);
    final Set<Pair<String,String>> positiveExamples = rudik.
        generatePositiveExamples(relationNames, subjectType, objectType);

    //compute outputs
    final List<HornRule> outputRules = rudik.
        discoverNegativeHornRules(negativeExamples, positiveExamples, relationNames, subjectType, objectType);
    
    final Instant endTime = Instant.now();
    LOGGER.info("----------------------------COMPUTATION ENDED----------------------------");
    LOGGER.info("Final computation time: {} seconds.",(endTime.toEpochMilli()-startTime.toEpochMilli())/1000.);
    LOGGER.info("----------------------------Final output rules----------------------------");
    for(final HornRule oneRule:outputRules){
      LOGGER.info("{}",oneRule);
    }
    return outputRules;
  }
  
  protected Map<HornRule,Double> executeRudikAllNegativeRules(final Set<String> relationNames, final String subjectType, final String objectType, final int maxRulesNumber){
    final Instant startTime = Instant.now();
    
    //get positive and negative examples
//    final Set<Pair<String,String>> negativeExamples = rudik.
//        generateNegativeExamples(relationNames, subjectType, objectType, false, false);
//    final Set<Pair<String,String>> positiveExamples = rudik.
//        generatePositiveExamples(relationNames, subjectType, objectType);
    
    String csvFile_positive = "/home/anish/fall17/rudik_new/Rudik/keyPerson_positive.csv";
    String csvFile_negative = "/home/anish/fall17/rudik_new/Rudik/keyPerson_negative.csv";
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
    }
    return outputRules;
  }
}
