
module Spa2015GitHub


  class Fork



    attr_reader :owner, :project_name

    def initialize(owner, project_name)
      @owner = owner
      @project_name = project_name
      @name = "#{@owner}/#{@project_name}"
    end

    def children(max_depth)
      return Spa2015GitHub::Client.get_forks(@name)
    end

  end
end