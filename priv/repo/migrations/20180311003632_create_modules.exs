defmodule Tigris.Repo.Migrations.CreateModules do
  use Ecto.Migration

  def change do
    create table(:modules) do

      timestamps()
    end

  end
end
