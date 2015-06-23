require_relative '../lib/spa2015/report/csv_writer'

describe Spa2015::Report::CsvWriter do

  before do
    @io = double('io')
    @writer = Spa2015::Report::CsvWriter.new(@io)
  end

  it 'should print out the parent if it has not children' do
    expect_written('user/name')

    @writer.generate_fork_report(mock_fork('user/name'),1)
  end


  it 'should print out the children of the parent' do
    fork = mock_fork('user/name', [mock_fork('child1/name'), mock_fork('child2/name')])
    expect_written('user/name,child1/name,child2/name')

    @writer.generate_fork_report(fork,2)
  end

  it 'should fetch pass through max depth to children to get all their children' do
    expect_written('user/name,child/name,grandchild/name')

    fork = mock_fork('user/name', [mock_fork('child/name'), mock_fork('grandchild/name')])

    expect(fork).to receive(:children).with(3)

    @writer.generate_fork_report(fork, 3)
  end

  it 'should die if a fork raises an error' do
    bad_fork = double('bad/data', :name => 'bad/data')
    allow(bad_fork).to receive(:children).and_raise('boom')
    fork = mock_fork('user/name', [bad_fork, mock_fork('child2/name')])

    expect{@writer.generate_fork_report(fork, 1)}.to raise_error
  end

  def mock_fork(name, children = [])
    double(name, :name => name, :children => children)
  end

  def expect_written(string)
    expect(@io).to receive(:puts).with(string)
  end

end