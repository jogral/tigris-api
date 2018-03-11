defmodule Tigris.Repo.Migrations.CreateNotifications do
  use Ecto.Migration

  def change do
    create table(:notifications) do

      timestamps()
    end

  end
end
