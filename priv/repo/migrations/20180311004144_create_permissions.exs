defmodule Tigris.Repo.Migrations.CreatePermissions do
  use Ecto.Migration

  def change do
    create table(:permissions) do

      timestamps()
    end

  end
end
