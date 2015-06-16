require_relative '../lib/spa2015/github/fork'

class Dummy

  def initialize
    @repos = {}
  end

  def client_add(parent, children)
    @repos[parent] = children
  end

  def get_forks(repo)
    @repos.fetch(repo){[]}
  end
end

def add_to_repos(path, child_name, num_children = nil)
  @dummy.client_add(path, [{:owner => {:login => child_name}, :name => 'project', :forks => num_children}])
end

def new_fork(string, string2)
  Spa2015::GitHub::Fork.new(string, string2, client: @dummy)
end

describe Spa2015::GitHub::Fork do

  before do
    @dummy = Dummy.new
  end

  it 'should return an empty list of forks when there are no forks' do
    expect(new_fork('owner', 'project').children(1)).to be_empty
  end

  it 'should return a single fork when a repository has one fork' do
    add_to_repos('parent/project', 'child')

    expect(new_fork('parent', 'project').children(1))
        .to contain_exactly(have_attributes(:owner => 'child', :project_name => 'project'))
  end

  it 'should fetch the forks of a child' do
    add_to_repos('parent/project', 'child')
    add_to_repos('child/project', 'grand_child')

    expect(new_fork('parent', 'project').children(2))
        .to contain_exactly(
                have_attributes(:owner => 'child', :project_name => 'project'),
                have_attributes(:owner => 'grand_child', :project_name => 'project')
            )
  end

  it 'should stop when it reaches the maximum depth' do
    add_to_repos('parent/project', 'child')
    add_to_repos('child/project', 'grand_child')

    expect(new_fork('parent', 'project').children(1))
        .to contain_exactly(have_attributes(:owner => 'child', :project_name => 'project'))
  end

  it 'should not fetch children from a child with no forks' do
    add_to_repos('parent/project', 'child', 0)
    add_to_repos('child/project', 'grand_child')

    expect(new_fork('parent', 'project').children(2))
        .to contain_exactly(have_attributes(:owner => 'child', :project_name => 'project'))
  end

end
