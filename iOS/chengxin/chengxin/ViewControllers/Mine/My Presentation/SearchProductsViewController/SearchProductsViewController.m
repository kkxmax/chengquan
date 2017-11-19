//
//  SearchProductsViewController.m
//  chengxin
//
//  Created by common on 7/31/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "SearchProductsViewController.h"
#import "ProductTableViewCell.h"
#import "ItemTableViewCell.h"
#import "Global.h"
#import "ProductDetailsViewController.h"
#import "HomeCommerceDetailViewController.h"
#import "HomeItemDetailViewController.h"

@interface SearchProductsViewController ()

@end

@implementation SearchProductsViewController
{
    NSMutableArray* aryProducts;
    NSMutableArray* aryItems;
    NSMutableArray* aryServices;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    selected_tab = em_Product;
    [self setTab:em_Product];
    
    
    aryItems = [[NSMutableArray alloc] init];
    aryServices = [[NSMutableArray alloc] init];
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [GeneralUtil showProgress];
    [self getList:em_Product For:nil];
}
-(void)getList:(SearchProductTab)tab For:(NSString*)keyword
{
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    if(tab == em_Product)
    {
        [dicParams setObject:@"getMyProductList" forKey:@"pAct"];
        if(keyword != nil && keyword.length > 0)
            [dicParams setObject:keyword forKey:@"keyword"];
        
        [[WebAPI sharedInstance] sendPostRequestWithUpload:@"getMyProductList" Parameters:dicParams :^(NSObject *resObj){
            
            [GeneralUtil hideProgress];
            
            NSDictionary *dicRes = (NSDictionary *)resObj;
            
            if (dicRes != nil ) {
                if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                    aryProducts = [[NSMutableArray alloc] init];
                    [aryProducts addObjectsFromArray:dicRes[@"data"]];
                    [self.tableView reloadData];
                }
                else {
                    [GeneralUtil alertInfo:dicRes[@"msg"]];
                }
            }
        }];
    }else if(tab == em_Item)
    {
        [dicParams setObject:@"getMyItemList" forKey:@"pAct"];
        if(keyword != nil && keyword.length > 0)
            [dicParams setObject:keyword forKey:@"keyword"];
        
        [[WebAPI sharedInstance] sendPostRequestWithUpload:@"getMyItemList" Parameters:dicParams :^(NSObject *resObj){
            
            [GeneralUtil hideProgress];
            
            NSDictionary *dicRes = (NSDictionary *)resObj;
            
            if (dicRes != nil ) {
                if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                    aryItems = [[NSMutableArray alloc] init];
                    [aryItems addObjectsFromArray:dicRes[@"data"]];
                    [self.tableView reloadData];
                }
                else {
                    [GeneralUtil alertInfo:dicRes[@"msg"]];
                }
            }
        }];

    }else if(tab == em_Service)
    {
        [dicParams setObject:@"getMyServiceList" forKey:@"pAct"];
        if(keyword != nil && keyword.length > 0)
            [dicParams setObject:keyword forKey:@"keyword"];
        
        [[WebAPI sharedInstance] sendPostRequestWithUpload:@"getMyServiceList" Parameters:dicParams :^(NSObject *resObj){
            
            [GeneralUtil hideProgress];
            
            NSDictionary *dicRes = (NSDictionary *)resObj;
            
            if (dicRes != nil ) {
                if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                    aryServices = [[NSMutableArray alloc] init];
                    [aryServices addObjectsFromArray:dicRes[@"data"]];
                    [self.tableView reloadData];
                }
                else {
                    [GeneralUtil alertInfo:dicRes[@"msg"]];
                }
            }
        }];

    }
}


- (void)setTab:(SearchProductTab)selection
{
    if(selection == em_Product)
    {
        self.lblProductUnderline.hidden = NO;
        self.lblItemUnderline.hidden = YES;
        self.lblServiceUnderline.hidden = YES;
        [self.btnProduct setTitleColor:RGB_COLOR(246,184,17) forState:UIControlStateNormal];
        [self.btnItem setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [self.btnService setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    }else if(selection == em_Item)
    {
        self.lblProductUnderline.hidden = YES;
        self.lblItemUnderline.hidden = NO;
        self.lblServiceUnderline.hidden = YES;
        [self.btnProduct setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [self.btnItem setTitleColor:RGB_COLOR(246,184,17) forState:UIControlStateNormal];
        [self.btnService setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    }else if(selection == em_Service)
    {
        self.lblProductUnderline.hidden = YES;
        self.lblItemUnderline.hidden = YES;
        self.lblServiceUnderline.hidden = NO;
        [self.btnProduct setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [self.btnItem setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [self.btnService setTitleColor:RGB_COLOR(246,184,17) forState:UIControlStateNormal];
    }
    selected_tab = selection;
}


-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(selected_tab == em_Product)
    {
        return aryProducts.count;
    }else if(selected_tab == em_Item)
    {
        return aryItems.count;
    }else if(selected_tab == em_Service)
    {
        return aryServices.count;
    }
    return 0;
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(selected_tab == em_Product)
    {
        return 163;
    }else if(selected_tab == em_Item)
    {
        return 190;
    }else
    {
        return 163;
    }
    return 163;
}
-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(selected_tab == em_Product)
    {
        ProductTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
        
        if(cell == nil)
        {
            cell = (ProductTableViewCell*)[[[NSBundle mainBundle] loadNibNamed:@"ProductTableViewCell" owner:self options:nil] objectAtIndex:0];
            
        }
        NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [aryProducts[indexPath.row] objectForKey:@"imgPath1"]]];
        [cell.photo setImage:[UIImage imageWithData:[NSData dataWithContentsOfURL:url]]];
        cell.name.text = [aryProducts[indexPath.row] objectForKey:@"name"];
        cell.price.text = [NSString stringWithFormat:@"¥%@", [[aryProducts[indexPath.row] objectForKey:@"price"] stringValue]];
        return cell;
    }else if(selected_tab == em_Item)
    {
        ItemTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
        
        if(cell == nil)
        {
            cell = (ItemTableViewCell*)[[[NSBundle mainBundle] loadNibNamed:@"ItemTableViewCell" owner:self options:nil] objectAtIndex:0];
            
        }
        
        NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [aryItems[indexPath.row] objectForKey:@"logo"]]];
        [cell.photo setImage:[UIImage imageWithData:[NSData dataWithContentsOfURL:url]]];
        cell.name.text = [aryItems[indexPath.row] objectForKey:@"name"];
        cell.comment.text = [aryItems[indexPath.row] objectForKey:@"comment"];
        cell.status.text = [aryItems[indexPath.row] objectForKey:@"statusName"];
        
        return cell;
    }else if(selected_tab == em_Service)
    {
        ItemTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
        
        if(cell == nil)
        {
            cell = (ItemTableViewCell*)[[[NSBundle mainBundle] loadNibNamed:@"ItemTableViewCell" owner:self options:nil] objectAtIndex:0];
            
        }
        
        NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [aryServices[indexPath.row] objectForKey:@"logo"]]];
        [cell.photo setImage:[UIImage imageWithData:[NSData dataWithContentsOfURL:url]]];
        cell.name.text = [aryServices[indexPath.row] objectForKey:@"name"];
        cell.comment.text = [aryServices[indexPath.row] objectForKey:@"comment"];
        cell.status.text = [aryServices[indexPath.row] objectForKey:@"statusName"];
        //cell.business.text = [aryServices[indexPath.row] objectForKey:@"statusName"];
        return cell;

    }
    
    return nil;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(selected_tab == em_Product)
    {
        HomeCommerceDetailViewController* vc = [[HomeCommerceDetailViewController alloc] initWithNibName:@"HomeCommerceDetailViewController" bundle:nil];
        [[CommonData sharedInstance] setSelectedProductID:[aryProducts[indexPath.row] objectForKey:@"id"]];
        [self.navigationController pushViewController:vc animated:YES];
    }else if(selected_tab == em_Item)
    {
        HomeItemDetailViewController* vc = [[HomeItemDetailViewController alloc] initWithNibName:@"HomeItemDetailViewController" bundle:nil];
        [[CommonData sharedInstance] setSelectedItemServiceDic:aryItems[indexPath.row]];
        [self.navigationController pushViewController:vc animated:YES];
    }else if(selected_tab == em_Service)
    {
        
    }
}
- (IBAction)onProduct:(id)sender
{
    [self setTab:em_Product];
    [self getList:em_Product For:@""];
    //[self.tableView reloadData];
}
- (IBAction)onItem:(id)sender
{
    [self setTab:em_Item];
    [self getList:em_Item For:@""];
    //[self.tableView reloadData];
}
- (IBAction)onService:(id)sender
{
    [self setTab:em_Service];
    [self getList:em_Service For:@""];
    //[self.tableView reloadData];
}
- (IBAction)onBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}
- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText
{
    
}
- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar
{
    
}
- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar
{
    [self getList:selected_tab For:searchBar.text];
}
@end
