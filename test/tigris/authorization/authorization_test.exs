defmodule Tigris.AuthorizationTest do
  use Tigris.DataCase

  alias Tigris.Authorization

  describe "groups" do
    alias Tigris.Authorization.Group

    @valid_attrs %{}
    @update_attrs %{}
    @invalid_attrs %{}

    def group_fixture(attrs \\ %{}) do
      {:ok, group} =
        attrs
        |> Enum.into(@valid_attrs)
        |> Authorization.create_group()

      group
    end

    test "list_groups/0 returns all groups" do
      group = group_fixture()
      assert Authorization.list_groups() == [group]
    end

    test "get_group!/1 returns the group with given id" do
      group = group_fixture()
      assert Authorization.get_group!(group.id) == group
    end

    test "create_group/1 with valid data creates a group" do
      assert {:ok, %Group{} = group} = Authorization.create_group(@valid_attrs)
    end

    test "create_group/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Authorization.create_group(@invalid_attrs)
    end

    test "update_group/2 with valid data updates the group" do
      group = group_fixture()
      assert {:ok, group} = Authorization.update_group(group, @update_attrs)
      assert %Group{} = group
    end

    test "update_group/2 with invalid data returns error changeset" do
      group = group_fixture()
      assert {:error, %Ecto.Changeset{}} = Authorization.update_group(group, @invalid_attrs)
      assert group == Authorization.get_group!(group.id)
    end

    test "delete_group/1 deletes the group" do
      group = group_fixture()
      assert {:ok, %Group{}} = Authorization.delete_group(group)
      assert_raise Ecto.NoResultsError, fn -> Authorization.get_group!(group.id) end
    end

    test "change_group/1 returns a group changeset" do
      group = group_fixture()
      assert %Ecto.Changeset{} = Authorization.change_group(group)
    end
  end

  describe "permissions" do
    alias Tigris.Authorization.Permission

    @valid_attrs %{}
    @update_attrs %{}
    @invalid_attrs %{}

    def permission_fixture(attrs \\ %{}) do
      {:ok, permission} =
        attrs
        |> Enum.into(@valid_attrs)
        |> Authorization.create_permission()

      permission
    end

    test "list_permissions/0 returns all permissions" do
      permission = permission_fixture()
      assert Authorization.list_permissions() == [permission]
    end

    test "get_permission!/1 returns the permission with given id" do
      permission = permission_fixture()
      assert Authorization.get_permission!(permission.id) == permission
    end

    test "create_permission/1 with valid data creates a permission" do
      assert {:ok, %Permission{} = permission} = Authorization.create_permission(@valid_attrs)
    end

    test "create_permission/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Authorization.create_permission(@invalid_attrs)
    end

    test "update_permission/2 with valid data updates the permission" do
      permission = permission_fixture()
      assert {:ok, permission} = Authorization.update_permission(permission, @update_attrs)
      assert %Permission{} = permission
    end

    test "update_permission/2 with invalid data returns error changeset" do
      permission = permission_fixture()
      assert {:error, %Ecto.Changeset{}} = Authorization.update_permission(permission, @invalid_attrs)
      assert permission == Authorization.get_permission!(permission.id)
    end

    test "delete_permission/1 deletes the permission" do
      permission = permission_fixture()
      assert {:ok, %Permission{}} = Authorization.delete_permission(permission)
      assert_raise Ecto.NoResultsError, fn -> Authorization.get_permission!(permission.id) end
    end

    test "change_permission/1 returns a permission changeset" do
      permission = permission_fixture()
      assert %Ecto.Changeset{} = Authorization.change_permission(permission)
    end
  end
end
