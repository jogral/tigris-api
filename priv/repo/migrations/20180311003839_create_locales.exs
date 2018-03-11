defmodule Tigris.Repo.Migrations.CreateLocales do
  use Ecto.Migration

  def change do
    create table(:locales) do

      timestamps()
    end

  end
end
