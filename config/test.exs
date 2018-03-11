use Mix.Config

# We don't run a server during test. If one is required,
# you can enable the server option below.
config :tigris, TigrisWeb.Endpoint,
  http: [port: 4001],
  server: false

# Print only warnings and errors during test
config :logger, level: :warn

# Configure your database
config :tigris, Tigris.Repo,
  adapter: Ecto.Adapters.Postgres,
  username: "tigris_user",
  password: "password1",
  database: "tigris_test",
  hostname: "localhost",
  pool: Ecto.Adapters.SQL.Sandbox
