require 'rspec'

require_relative '../lib/spa2015/github/fork'

class Dummy
  def method_missing(m, *args, &block)
    puts "There's no method called #{m} here -- please try again."
  end
end

describe 'GitHub forks' do

  Spa2015::Github::Client = Dummy

  it 'should do something' do
    Spa2015::Github::Fork.parent('thing')
  end
end