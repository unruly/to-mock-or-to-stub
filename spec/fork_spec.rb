require 'spa2015_github/fork'

class Dummy
  def method_missing(m, *args, &block)
    puts "There's no method called #{m} here -- please try again."
  end


  def client_returns(repositories)
    @repos = repositories
  end

  def get_forks(repo)
    @repos
  end
end

describe Spa2015GitHub::Fork do

  Spa2015GitHub::Client = Dummy.new

  it 'should return an empty list of forks when there are no forks' do
    Spa2015GitHub::Client.client_returns([])

    expect(Spa2015GitHub::Fork.new('owner', 'project').children(1)).to be_empty
  end

  it "should return a single fork when a repository has one fork" do
    Spa2015GitHub::Client.client_returns([{:owner => 'child', :name => 'project'}])

    expect(Spa2015GitHub::Fork.new('owner', 'project').children(1)).to contain_exactly(:owner => 'child', :name => 'project')
  end
end


