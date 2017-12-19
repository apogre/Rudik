package asu.edu.neg_rule_miner.dbpedia;

import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Sets;

import asu.edu.neg_rule_miner.ClientTest;
import asu.edu.neg_rule_miner.configuration.ConfigurationFacility;

public class DBPediaClient extends ClientTest{
  
  private final static String CONF_FILE = "src/main/config/DbpediaConfiguration.xml";
  
  @BeforeClass
  public static void bringUp(){
    //set config file
    ConfigurationFacility.setConfigurationFile(CONF_FILE);
  }
  
  
//  @Test
//  public void testSpouseNegative() {
//    final Set<String> relations = Sets.newHashSet("http://dbpedia.org/ontology/keyPerson");
//    final String typeSubject = "http://dbpedia.org/ontology/Company";
//    final String typeObject = "http://dbpedia.org/ontology/Person";
//    Assert.assertNotNull(super.executeRudikNegativeRules(relations, typeSubject, typeObject)); 
//  }
  
//  @Test
//  public void testSpousePositive() {
//    final Set<String> relations = Sets.newHashSet("http://dbpedia.org/ontology/keyPerson");
//    final String typeSubject = "http://dbpedia.org/ontology/Company";
//    final String typeObject = "http://dbpedia.org/ontology/Person";
//    Assert.assertNotNull(super.executeRudikPositiveRules(relations, typeSubject, typeObject)); 
//  }
  
  @Test
  public void testSpouseAllNegative() {
    //target relations to be discovered
    final Set<String> relations = Sets.newHashSet("http://dbpedia.org/ontology/keyPerson");
    //type of the subject according to the ontology
    final String typeSubject = "http://dbpedia.org/ontology/Company";
    //type of the object according to the ontology
    final String typeObject = "http://dbpedia.org/ontology/Person";
    //max number of rules to have in output. If set to a negative number, it will return all the ruls
    final int maxRulesNumber = 1000;
    Assert.assertNotNull(super.executeRudikAllNegativeRules(relations, typeSubject, typeObject,maxRulesNumber)); 
  }
  
  @Test
  public void testSpouseAllPositive() {
    //target relations to be discovered
    final Set<String> relations = Sets.newHashSet("http://dbpedia.org/ontology/keyPerson");
    //type of the subject according to the ontology
    final String typeSubject = "http://dbpedia.org/ontology/Company";
    //type of the object according to the ontology
    final String typeObject = "http://dbpedia.org/ontology/Person";
    //max number of rules to have in output. If set to a negative number, it will return all the ruls
    final int maxRulesNumber = 1000;
    Assert.assertNotNull(super.executeRudikAllPositiveRules(relations, typeSubject, typeObject,maxRulesNumber));
  }

}
