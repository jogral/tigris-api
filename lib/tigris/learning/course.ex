defmodule Tigris.Learning.Course.TitleSlug do
  use EctoAutoslugField.Slug, from: :title, to: :slug
end

defmodule Tigris.Learning.Course do
  use Ecto.Schema
  import Ecto.Changeset
  alias Tigris.Learning.Course


  schema "courses" do
    field :title, :string
    field :slug, TitleSlug.Type
    field :description, :string
    field :long_description, :string
    field :image, :string
    field :status, :integer, default: 0
    belongs_to :creator, Tigris.Users.User
    has_many :tags, Tigris.Learning.Tag
    timestamps()
  end

  @doc false
  def changeset(%Course{} = course, attrs) do
    course
    |> cast(attrs, [:title, :description, :status, :image, :long_description])
    |> validate_required([:title, :description])
    |> validate_number(:status, greater_than_or_equal_to: -1, less_than_or_equal_to: 1)
    |> unique_constraint(:title)
    |> TitleSlug.maybe_generate_slug
    |> TitleSlug.unique_constraint
  end
end
