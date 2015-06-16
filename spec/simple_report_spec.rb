require 'spa2015/report/csv_writer'

class StubIO

  attr_reader :lines

  def initialize
    @lines = []
  end

  def puts(str)
    @lines << str
  end
end

describe Spa2015::Report::CsvWriter do



end