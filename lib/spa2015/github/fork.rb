
module Spa2015
  module GitHub

    class Fork

      attr_reader :owner, :project_name, :name

      def initialize(owner, project_name, known_children = nil)
        @owner = owner
        @project_name = project_name
        @name = "#{@owner}/#{@project_name}"
        @known_children = known_children
      end

      def children(max_depth)
        if max_depth <= 0
          return []
        end
        immediate = fetch_children

        theirs = immediate.map{ |child|
          child.children(max_depth - 1)
        }.flatten

        immediate.concat(theirs)
      end

      def to_s
        "Fork(#{@name})"
      end

      private_methods :fetch_children

      def fetch_children
        if @known_children == 0
          return []
        end
        Spa2015::GitHub::Client.get_forks(@name).map{|fork|
          Fork.new(fork[:owner][:login], fork[:name], fork[:forks])
        }
      end

    end
  end

end