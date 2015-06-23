module Spa2015
  module Report
    class HandlesBadForksReportWriter

      def initialize(stream = $stdout)
        @stream = stream
      end

      def generate_fork_report(parent, max_depth)

        if max_depth <= 0
          return;
        end

        begin
          children = parent.children(1)

          @stream.puts parent.name
          children.each{|fork|
            generate_fork_report(fork, max_depth-1)
          }
        rescue Exception
          @stream.puts("*** INVALID: #{parent.name}")
        end
      end

    end
  end
end