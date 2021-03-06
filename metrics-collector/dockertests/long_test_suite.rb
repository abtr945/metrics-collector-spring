require 'minitest/autorun'

class LongTestSuite < MiniTest::Unit::TestCase
  
  #override random test run ordering
  i_suck_and_my_tests_are_order_dependent!
  
  
  # Test 1 - setting credentials
  def test_set_credentials
     x = 'AKIAI6M2PRETOEO5ILLAandebYwiKBHNEa%2BRy/a4gyiKHCfFY/wVc9Q%2BokI/1sPandap-southeast-2'
     x1 = x[0..19]
     x2 = x[23..66]
     x3 = x[70..83]
     str = %Q[curl --data 'awsAccessKeyId=#{x1}&awsSecretAccessKey=#{x2}&region=#{x3}' http://localhost:8080/metrics/aws/credentials]
     output = `#{str}`
     assert_equal "", output, "TEST 1: set aws credentials - FAILED"
  end
  
  
  # Test 2 - Start and stop new experiment, collect associated metrics
  def test_new_experiment
     output = `curl --data 'asgName=test&totalInstances=5' http://localhost:8080/metrics/experiments/start`
     refute_match "<html>", output, "TEST 2.1: start new experiment - FAILED"
     
     sleep(80)
     
     output2 = `curl -X PUT http://localhost:8080/metrics/experiments/stop/#{output}`
     refute_match "<html>", output2, "TEST 2.2: stop the newly started experiment - FAILED"
     
     str = %Q[curl --data 'experimentId=#{output}' http://localhost:8080/metrics/metrics/aggregated]
     
     output3 = `#{str}`
     refute_match "<html>", output3, "TEST 2.3: collect aggregated metrics for the newly created experiment - FAILED"
     
  end
  
  
end
