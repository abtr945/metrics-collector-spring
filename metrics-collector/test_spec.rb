describe "Docker Image" do

  before(:all) do
    @image = Docker::Image.all.detect{
      |image| image.info["Repository"] == "abtran/metrics-collector:10"
    }
  end

  it "should exist" do
    expect(@image).not_to be_nil
  end

  it "should expose port 8080" do
    expect(@image.json["config"]["ExposedPorts"].has_key?("8080")).to be_true
  end

end  
