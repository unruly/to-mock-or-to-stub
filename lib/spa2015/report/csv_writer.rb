module Spa2015
  module Report
    class CsvWriter

      def initialize(stream = $stdout)
        @stream = stream
      end

      def generate_fork_report(parent, max_depth)
        names = parent.children(max_depth).map{|child|
            child.name
        }
        names.unshift(parent.name)
        @stream.puts names.join(',')
      end

    end
  end
end