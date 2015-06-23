require_relative '../lib/spa2015/github/fork'



describe Spa2015::GitHub::Fork do

  def new_fork(string, string2)
    Spa2015::GitHub::Fork.new(string, string2, client: @mock_client)
  end

  def add_to_repos(path, children, num_children = nil)
    allow(@mock_client).to receive(:get_forks)
                               .with(path)
                               .and_return(to_fork(children, num_children))
  end

  def to_fork(children, num_children)
    children.map{ |child|
      {:owner => {:login => child}, :name => 'project', :forks => num_children}
    }
  end

  before do
    @mock_client = double('client')
    allow(@mock_client).to receive(:get_forks).and_return([])
  end

  it 'should return an empty list of forks when there are no forks' do
    expect(new_fork('owner', 'project').children(1)).to be_empty
  end

  it 'should return a single fork when a repository has one fork' do
    add_to_repos('parent/project', ['child'])

    expect(new_fork('parent', 'project').children(1))
        .to contain_exactly(have_attributes(:owner => 'child', :project_name => 'project'))
  end

  it 'should fetch the forks of a child' do
    add_to_repos('parent/project', ['child'])
    add_to_repos('child/project', ['grand_child'])

    expect(new_fork('parent', 'project').children(2))
        .to contain_exactly(
                have_attributes(:owner => 'child', :project_name => 'project'),
                have_attributes(:owner => 'grand_child', :project_name => 'project')
            )
  end

  it 'should fetch all the forks' do
    add_to_repos('parent/project', ['child1', 'child2'])


    expect(new_fork('parent', 'project').children(1))
        .to contain_exactly(
                have_attributes(:owner => 'child1', :project_name => 'project'),
                have_attributes(:owner => 'child2', :project_name => 'project')
            )
  end

  it 'should stop when it reaches the maximum depth' do
    add_to_repos('parent/project', ['child'])
    add_to_repos('child/project', ['grand_child'])

    expect(new_fork('parent', 'project').children(1))
        .to contain_exactly(have_attributes(:owner => 'child', :project_name => 'project'))
  end

  it 'should not fetch children from a child with no forks' do
    add_to_repos('parent/project', ['child'], 0)
    add_to_repos('child/project', ['grand_child'])

    expect(new_fork('parent', 'project').children(2))
        .to contain_exactly(have_attributes(:owner => 'child', :project_name => 'project'))
  end

end