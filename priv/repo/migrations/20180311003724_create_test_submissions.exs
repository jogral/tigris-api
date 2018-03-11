defmodule Tigris.Repo.Migrations.CreateTestSubmissions do
  use Ecto.Migration

  def change do
    create table(:test_submissions) do

      timestamps()
    end

  end
end
