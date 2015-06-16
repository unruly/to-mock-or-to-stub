require 'spa2015/github/fork'

class Dummy

  def initialize
    @repos = {}
  end

  def client_add(parent, children)
    @repos[parent] = children
  end

  def get_forks(repo)
    if @repos.has_key?(repo)
      @repos[repo]
    else
      []
    end
  end
end

def add_to_repos(path, child_name, num_children = nil)
  Spa2015::GitHub::Client.client_add(path, [{:owner => {:login => child_name}, :name => 'project', :forks => num_children}])
end

describe Spa2015::GitHub::Fork do

  before do
    Spa2015::GitHub::Client = Dummy.new
  end

  it 'should return an empty list of forks when there are no forks' do
    expect(Spa2015::GitHub::Fork.new('owner', 'project').children(1)).to be_empty
  end

  it "should return a single fork when a repository has one fork" do
    add_to_repos('parent/project', 'child')

    expect(Spa2015::GitHub::Fork.new('parent', 'project').children(1))
        .to contain_exactly(have_attributes(:owner => 'child', :project_name => 'project'))
  end

  it "should fetch the forks of a child" do
    add_to_repos('parent/project', 'child')
    add_to_repos('child/project', 'grand_child')

    expect(Spa2015::GitHub::Fork.new('parent', 'project').children(2))
        .to contain_exactly(
                have_attributes(:owner => 'child', :project_name => 'project'),
                have_attributes(:owner => 'grand_child', :project_name => 'project')
            )
  end

  it 'should stop when it reaches the maximum depth' do
    add_to_repos('parent/project', 'child')
    add_to_repos('child/project', 'grand_child')

    expect(Spa2015::GitHub::Fork.new('parent', 'project').children(1))
        .to contain_exactly(have_attributes(:owner => 'child', :project_name => 'project'))
  end

  it 'should not fetch children from a child with no forks' do
    add_to_repos('parent/project', 'child', 0)
    add_to_repos('child/project', 'grand_child')

    expect(Spa2015::GitHub::Fork.new('parent', 'project').children(2))
        .to contain_exactly(have_attributes(:owner => 'child', :project_name => 'project'))
  end

end
