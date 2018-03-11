defmodule Tigris.Repo.Migrations.CreateQuizSubmissions do
  use Ecto.Migration

  def change do
    create table(:quiz_submissions) do

      timestamps()
    end

  end
end
