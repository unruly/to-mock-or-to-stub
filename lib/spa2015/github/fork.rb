
module Spa2015
  module GitHub

    class Fork

      attr_reader :owner, :project_name, :name

      def initialize(owner, project_name, known_children: nil, client: Spa2015::GitHub::Client.new)
        @owner = owner
        @project_name = project_name
        @name = "#{@owner}/#{@project_name}"
        @known_children = known_children
        @client = client
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
        @client.get_forks(@name).map{|fork|
          Fork.new(fork[:owner][:login], fork[:name], known_children: fork[:forks], client: @client)
        }
      end

    end
  end

end