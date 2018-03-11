defmodule TigrisWeb.API.V2.UserController do
  use TigrisWeb, :controller

  # alias Tigris.Users
  # alias Tigris.Users.User

  action_fallback TigrisWeb.FallbackController

  def index(conn, _params) do
    users = [ExOauth2Provider.Plug.current_resource_owner(conn)]
    render(conn, "index.json", users: users)
  end
end
