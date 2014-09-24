require 'minitest/autorun'

class ShortTestSuite < MiniTest::Unit::TestCase
  
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
  
  
  # Test 2 - Show not existing experiment
  def test_show_not_exist_experiment
     not_exist_id = '10000'
     output = `curl http://localhost:8080/metrics/experiments/#{not_exist_id}.json`
     assert_match "<html>", output, "TEST 2: show not existing experiment - FAILED"
  end
  
  
  
  
  
  # Test 4 - Show not existing metric
  def test_show_not_exist_metric
     not_exist_id = '10000'
     output = `curl http://localhost:8080/metrics/metrics/#{not_exist_id}.json`
     assert_match "<html>", output, "TEST 4: show not existing metric - FAILED"
  end
  
  
  
end
  
