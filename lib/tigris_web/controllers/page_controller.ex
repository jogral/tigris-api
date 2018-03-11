defmodule TigrisWeb.PageController do
  use TigrisWeb, :controller

  def index(conn, _params) do
    render conn, "index.html"
  end
end
