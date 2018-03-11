# This file is responsible for configuring your application
# and its dependencies with the aid of the Mix.Config module.
#
# This configuration file is loaded before any dependency and
# is restricted to this project.
use Mix.Config

# General application configuration
config :tigris,
  ecto_repos: [Tigris.Repo]

# Configures the endpoint
config :tigris, TigrisWeb.Endpoint,
  url: [host: "localhost"],
  secret_key_base: "BGMvMhrd7xuRy9JXVT4uF2+LRCZaDQiafUozYMAEixfLemuYJhXMsOLob4cNPlpS",
  render_errors: [view: TigrisWeb.ErrorView, accepts: ~w(html json)],
  pubsub: [name: Tigris.PubSub,
           adapter: Phoenix.PubSub.PG2]

# Configures Elixir's Logger
config :logger, :console,
  format: "$time $metadata[$level] $message\n",
  metadata: [:request_id]

# Import environment specific config. This must remain at the bottom
# of this file so it overrides the configuration defined above.
import_config "#{Mix.env}.exs"

# %% Coherence Configuration %%   Don't remove this line
config :coherence,
  user_schema: Tigris.Users.User,
  repo: Tigris.Repo,
  module: Tigris,
  web_module: TigrisWeb,
  router: TigrisWeb.Router,
  messages_backend: TigrisWeb.Coherence.Messages,
  logged_out_url: "/",
  email_from_name: "Your Name",
  email_from_email: "yourname@example.com",
  opts: [:authenticatable, :recoverable, :lockable, :trackable, :unlockable_with_token, :confirmable, :registerable]

config :coherence, TigrisWeb.Coherence.Mailer,
  adapter: Swoosh.Adapters.Sendgrid,
  api_key: "your api key here"
# %% End Coherence Configuration %%

config :phoenix_oauth2_provider, PhoenixOauth2Provider,
  module: Tigris,
  current_resource_owner: :current_user,
  repo: Tigris.Repo,
  resource_owner: Tigris.Users.User,
  use_refresh_token: true

config :cors_plug,
  origin: ["*"],
  max_age: 86400,
  methods: ["GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"]
