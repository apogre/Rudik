package asu.edu.neg_rule_miner.dbpedia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Sets;

import asu.edu.neg_rule_miner.ClientTest;
import asu.edu.neg_rule_miner.configuration.ConfigurationFacility;
import au.com.bytecode.opencsv.CSVReader;

public class DBPediaClient extends ClientTest{
  
  private final static String CONF_FILE = "src/main/config/DbpediaConfiguration.xml";
  private static final String data_path = "/home/apradhan/proj/fact_check_kb/dataset/dataset_sample.csv";
  
  @BeforeClass
  public static void bringUp(){
    //set config file
    ConfigurationFacility.setConfigurationFile(CONF_FILE);
  }
  
  public List<String> input_predicates(String csvFile) {
	  System.out.println(csvFile);
      List<String> csvList = new ArrayList<String>();
      try (BufferedReader br = new BufferedReader(new FileReader(csvFile))){
          String line;
                  
          while ((line = br.readLine()) != null) {
//        	  System.out.println(line);
//        	  System.out.println(line[0] +" , " + line[1] + " , " + line[2]);
        	  csvList.add(line);        	  
          }

      } catch (IOException e) {
          e.printStackTrace();
      } 
	  return csvList;
  }
  
  
  @Test
  public void testSpouseNegative() {
  	List<String> tripleList = input_predicates(data_path);
  	String prefix = "http://dbpedia.org/ontology/";
  	for (int i = 0; i < tripleList.size(); i++){
  		String[] triple = tripleList.get(i).split(",");
  		final Set<String> relations = Sets.newHashSet(prefix+triple[1]);
	    final String typeSubject = prefix+triple[0];
	    final String typeObject = prefix+triple[2];
	    String[] sizes = {"1k","5k","10k"};
	    for (String size: sizes){
	    	Assert.assertNotNull(super.executeRudikNegativeRules(relations, typeSubject, typeObject,triple[1],size)); 
		    }	  		
	  	}
	  	}
  
  
  @Test
  public void testSpousePositive() {
	List<String> tripleList = input_predicates(data_path);
	String prefix = "http://dbpedia.org/ontology/";	
//	String[] triples = {"Person","child","Person"};
	for (int i = 0; i < tripleList.size(); i++){
		String[] triple = tripleList.get(i).split(",");
	    final Set<String> relations = Sets.newHashSet(prefix+triple[1]);
	    final String typeSubject = prefix+triple[0];
	    final String typeObject = prefix+triple[2];
	    String[] sizes = {"1k","5k","10k"};
	    for (String size: sizes){
	    	Assert.assertNotNull(super.executeRudikPositiveRules(relations, typeSubject, typeObject, triple[1],size));
	    }
	}
  }
//  
//  @Test
//  public void testSpouseAllNegative() {
//    //target relations to be discovered
//    final Set<String> relations = Sets.newHashSet("http://dbpedia.org/ontology/spouse");
//    //type of the subject according to the ontology
//    final String typeSubject = "http://dbpedia.org/ontology/Person";
//    //type of the object according to the ontology
//    final String typeObject = "http://dbpedia.org/ontology/Person";
//    //max number of rules to have in output. If set to a negative number, it will return all the ruls
//    final int maxRulesNumber = 1000;
//    Assert.assertNotNull(super.executeRudikAllNegativeRules(relations, typeSubject, typeObject,maxRulesNumber)); 
//  }
//  
//  @Test
//  public void testSpouseAllPositive() {
//    //target relations to be discovered
//    final Set<String> relations = Sets.newHashSet("http://dbpedia.org/ontology/spouse");
//    //type of the subject according to the ontology
//    final String typeSubject = "http://dbpedia.org/ontology/Person";
//    //type of the object according to the ontology
//    final String typeObject = "http://dbpedia.org/ontology/Person";
//    //max number of rules to have in output. If set to a negative number, it will return all the ruls
//    final int maxRulesNumber = 10;
//    Assert.assertNotNull(super.executeRudikAllPositiveRules(relations, typeSubject, typeObject,maxRulesNumber));
//  }

}
