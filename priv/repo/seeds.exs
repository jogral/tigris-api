# Script for populating the database. You can run it as:
#
#     mix run priv/repo/seeds.exs
#
# Inside the script, you can read and write to any of your
# repositories directly:
#
#     Tigris.Repo.insert!(%Tigris.SomeSchema{})
#
# We recommend using the bang functions (`insert!`, `update!`
# and so on) as they will fail if something goes wrong.

Tigris.Repo.delete_all Tigris.Users.User

Tigris.Users.User.changeset(%Tigris.Users.User{}, %{name: "Test User", email: "testuser@example.com", password: "secret", password_confirmation: "secret"})
|> Tigris.Repo.insert!
|> Coherence.ControllerHelpers.confirm!
