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
#import "HomeCommerceAddViewController.h"
#import "HomeItemAddViewController.h"
#import "UIImageView+WebCache.h"
#import "ItemDetailViewController.h"
#import "AAPullToRefresh.h"

@interface SearchProductsViewController ()

@end
@implementation SearchProductsViewController
{
    NSMutableArray* aryProducts;
    NSMutableArray* aryItems;
    NSMutableArray* aryServices;
    NSMutableArray* arySearchedProducts;
    NSMutableArray* arySearchedItems;
    NSMutableArray* arySearchedServices;
    NSNumber* currentID;
    UIAlertView* deleteAlert, *upDownAlert;
    UITableViewCell* currentCell;
    
    AAPullToRefresh *topRefreshView;
    AAPullToRefresh *bottomRefreshView;
    
    NSInteger refreshProductStartIndex;
    NSInteger refreshItemStartIndex;
    NSInteger refreshServiceStartIndex;
}
@synthesize selected_tab;

- (void)viewDidLoad {
    [super viewDidLoad];
    
//    selected_tab = em_Product;
    [self setTab:selected_tab];
    
    arySearchedProducts = [[NSMutableArray alloc] init];
    arySearchedItems = [[NSMutableArray alloc] init];
    arySearchedServices = [[NSMutableArray alloc] init];

    aryProducts = [[NSMutableArray alloc] init];
    aryItems = [[NSMutableArray alloc] init];
    aryServices = [[NSMutableArray alloc] init];
    
    [self.tableView registerNib:[UINib nibWithNibName:@"ProductTableViewCell" bundle:nil] forCellReuseIdentifier:@"ProductCellIdentifier"];
    [self.tableView registerNib:[UINib nibWithNibName:@"ItemTableViewCell" bundle:nil] forCellReuseIdentifier:@"ItemCellIdentifier"];
    
    __weak typeof(self) weakSelf = self;
    topRefreshView = [self.tableView addPullToRefreshPosition:AAPullToRefreshPositionTop ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshTopItems];
    }];
    bottomRefreshView = [self.tableView addPullToRefreshPosition:AAPullToRefreshPositionBottom ActionHandler:^(AAPullToRefresh *v){
        [weakSelf refreshBottomItems];
    }];
    
    refreshProductStartIndex = 0;
    refreshItemStartIndex = 0;
    refreshServiceStartIndex = 0;

    
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [[UISearchBar appearance] setBackgroundImage:[UIImage imageNamed:@"transparent.png"]];
    
    for(UIView* i in [self.searchBar subviews])
    {
        if([i isKindOfClass:[UITextField class]])
        {
            [((UITextField*)i) setBorderStyle:UITextBorderStyleNone];
        }
        for(UIView* j in [i subviews])
        {
            if([j isKindOfClass:[UITextField class]])
            {
                [((UITextField*)j) setBorderStyle:UITextBorderStyleNone];
            }
        }
    }
    
//    [self getList:selected_tab For:nil];
    
    [self refreshTopItems];
    topRefreshView.showPullToRefresh = YES;
    bottomRefreshView.showPullToRefresh = YES;

    if([CommonData sharedInstance].isPublished)
    {
        if(selected_tab == em_Product)
        {
            [appDelegate.window makeToast:@"产品已发布！"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
        }else if(selected_tab == em_Item)
        {
            [appDelegate.window makeToast:@"项目已发布！"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
        }else if(selected_tab == em_Service)
        {
            [appDelegate.window makeToast:@"服务已发布！"
                        duration:3.0
                        position:CSToastPositionCenter
                           style:nil];
        }
        [CommonData sharedInstance].isPublished = false;
    }
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    topRefreshView.showPullToRefresh = NO;
    bottomRefreshView.showPullToRefresh = NO;
}

- (void)refreshTopItems {
    if(selected_tab == em_Product) {
        refreshProductStartIndex = 0;
        if(aryProducts)
            [aryProducts removeAllObjects];
    }else if(selected_tab == em_Item){
        refreshItemStartIndex = 0;
        if(aryItems)
            [aryItems removeAllObjects];
    }else if(selected_tab == em_Service){
        refreshServiceStartIndex = 0;
        if(aryServices)
            [aryServices removeAllObjects];
    }
    [self getList:selected_tab For:self.searchBar.text];
}

- (void)refreshBottomItems {
    if(selected_tab == em_Product) {
        refreshProductStartIndex = aryProducts.count;
    }else if(selected_tab == em_Item){
        refreshItemStartIndex = aryItems.count;
    }else if(selected_tab == em_Service){
        refreshServiceStartIndex = aryServices.count;
    }
    [self getList:selected_tab For:self.searchBar.text];
}

-(void)getList:(SearchProductTab)tab For:(NSString*)keyword
{
    
    if(tab == em_Product)
    {
        [GeneralUtil showProgress];
        NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
        [dicParams setObject:@"getMyProductList" forKey:@"pAct"];
        if(keyword != nil && keyword.length > 0)
            [dicParams setObject:keyword forKey:@"keyword"];
        [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
        [dicParams setObject:[NSNumber numberWithInt:refreshProductStartIndex] forKey:@"start"];
        [dicParams setObject:[NSNumber numberWithInt:REFRESH_GET_DATA_COUNT] forKey:@"length"];
        [[WebAPI sharedInstance] sendPostRequest:@"getMyProductList" Parameters:dicParams :^(NSObject *resObj){
            
            [GeneralUtil hideProgress];
            
            NSDictionary *dicRes = (NSDictionary *)resObj;
            [topRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
            [bottomRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
            if (dicRes != nil ) {
                if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                    [aryProducts addObjectsFromArray:dicRes[@"data"]];
                    [self searchBar:self.searchBar textDidChange:self.searchBar.text];
                    if(aryProducts.count == 0) {
                        self.blankView.hidden = NO;
                    }else{
                        self.blankView.hidden = YES;
                    }
                    [self.tableView reloadData];
                }
                else {
                    
                    [appDelegate.window makeToast:dicRes[@"msg"]
                                duration:3.0
                                position:CSToastPositionCenter
                                   style:nil];
                }
            }
        }];
    }else if(tab == em_Item)
    {
        [GeneralUtil showProgress];
        NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
        [dicParams setObject:@"getMyItemList" forKey:@"pAct"];
        if(keyword != nil && keyword.length > 0)
            [dicParams setObject:keyword forKey:@"keyword"];
        [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
        [dicParams setObject:[NSNumber numberWithInt:refreshItemStartIndex] forKey:@"start"];
        [dicParams setObject:[NSNumber numberWithInt:REFRESH_GET_DATA_COUNT] forKey:@"length"];

        [[WebAPI sharedInstance] sendPostRequest:@"getMyItemList" Parameters:dicParams :^(NSObject *resObj){
            
            [GeneralUtil hideProgress];
            [topRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
            [bottomRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
            NSDictionary *dicRes = (NSDictionary *)resObj;
            
            if (dicRes != nil ) {
                if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                    [aryItems addObjectsFromArray:dicRes[@"data"]];
                    [self searchBar:self.searchBar textDidChange:self.searchBar.text];
                    if(aryItems.count == 0) {
                        self.blankView.hidden = NO;
                    }else{
                        self.blankView.hidden = YES;
                    }
                    [self.tableView reloadData];
                }
                else {
                    [appDelegate.window makeToast:dicRes[@"msg"]
                                duration:3.0
                                position:CSToastPositionCenter
                                   style:nil];
                }
            }
        }];

    }else if(tab == em_Service)
    {
        [GeneralUtil showProgress];
        NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
        [dicParams setObject:@"getMyServiceList" forKey:@"pAct"];
        if(keyword != nil && keyword.length > 0)
            [dicParams setObject:keyword forKey:@"keyword"];
        [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
        [dicParams setObject:[NSNumber numberWithInt:refreshServiceStartIndex] forKey:@"start"];
        [dicParams setObject:[NSNumber numberWithInt:REFRESH_GET_DATA_COUNT] forKey:@"length"];

        [[WebAPI sharedInstance] sendPostRequest:@"getMyServiceList" Parameters:dicParams :^(NSObject *resObj){
            
            [GeneralUtil hideProgress];
            
            NSDictionary *dicRes = (NSDictionary *)resObj;
            [topRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];
            [bottomRefreshView performSelector:@selector(stopIndicatorAnimation) withObject:nil afterDelay:0.1f];

            if (dicRes != nil ) {
                if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                    [aryServices addObjectsFromArray:dicRes[@"data"]];
                    [self searchBar:self.searchBar textDidChange:self.searchBar.text];
                    if(aryServices.count == 0) {
                        self.blankView.hidden = NO;
                    }else{
                        self.blankView.hidden = YES;
                    }
                    [self.tableView reloadData];
                }
                else {
                    [appDelegate.window makeToast:dicRes[@"msg"]
                                duration:3.0
                                position:CSToastPositionCenter
                                   style:nil];
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
        
        int akind = [[CommonData sharedInstance].userInfo[@"akind"] intValue];
        if(akind == 1)
            self.btnWrite.hidden = YES;
        else
            self.btnWrite.hidden = NO;
    }else if(selection == em_Item)
    {
        self.lblProductUnderline.hidden = YES;
        self.lblItemUnderline.hidden = NO;
        self.lblServiceUnderline.hidden = YES;
        [self.btnProduct setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [self.btnItem setTitleColor:RGB_COLOR(246,184,17) forState:UIControlStateNormal];
        [self.btnService setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        self.btnWrite.hidden = NO;
    }else if(selection == em_Service)
    {
        self.lblProductUnderline.hidden = YES;
        self.lblItemUnderline.hidden = YES;
        self.lblServiceUnderline.hidden = NO;
        [self.btnProduct setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [self.btnItem setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [self.btnService setTitleColor:RGB_COLOR(246,184,17) forState:UIControlStateNormal];
        self.btnWrite.hidden = NO;
    }
    selected_tab = selection;
}


-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(selected_tab == em_Product)
    {
        return arySearchedProducts.count;
    }else if(selected_tab == em_Item)
    {
        return arySearchedItems.count;
    }else if(selected_tab == em_Service)
    {
        return arySearchedServices.count;
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
        return 190;
    }
    return 163;
}
-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(selected_tab == em_Product)
    {
        ProductTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ProductCellIdentifier"];
        if(arySearchedProducts.count <= indexPath.row)
            return cell;
        [cell setDelegate:self];
        NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [arySearchedProducts[indexPath.row] objectForKey:@"imgPath1"]]];
        [cell.photo sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"no_image.png"]];
        cell.name.text = [arySearchedProducts[indexPath.row] objectForKey:@"name"];
        cell.price.text = [NSString stringWithFormat:@"¥%.2f", (float)[[arySearchedProducts[indexPath.row] objectForKey:@"price"] floatValue]];
        cell.productID = [arySearchedProducts[indexPath.row] objectForKey:@"id"];
        
        [cell setIsUp:[[arySearchedProducts[indexPath.row] objectForKey:@"status"] boolValue]];
        return cell;
    }else if(selected_tab == em_Item)
    {
        ItemTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ItemCellIdentifier"];
        if(arySearchedItems.count <= indexPath.row)
            return cell;
        [cell setDelegate:self];
        NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [arySearchedItems[indexPath.row] objectForKey:@"logo"]]];
        [cell.photo sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"no_image.png"]];
        cell.name.text = [arySearchedItems[indexPath.row] objectForKey:@"name"];
        cell.comment.text = [arySearchedItems[indexPath.row] objectForKey:@"comment"];
        cell.status.text = [arySearchedItems[indexPath.row] objectForKey:@"statusName"];
        cell.itemID = [arySearchedItems[indexPath.row] objectForKey:@"id"];
        [cell.business setTitle:arySearchedItems[indexPath.row][@"fenleiName"] forState:UIControlStateNormal];
        [cell setIsUp:[[arySearchedItems[indexPath.row] objectForKey:@"status"] boolValue]];
        return cell;
    }else if(selected_tab == em_Service)
    {
        ItemTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ItemCellIdentifier"];
        if(arySearchedServices.count <= indexPath.row)
            return cell;

        [cell setDelegate:self];
        NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, [arySearchedServices[indexPath.row] objectForKey:@"logo"]]];
        [cell.photo sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"no_image.png"]];
        cell.name.text = [arySearchedServices[indexPath.row] objectForKey:@"name"];
        cell.comment.text = [arySearchedServices[indexPath.row] objectForKey:@"comment"];
        cell.status.text = [arySearchedServices[indexPath.row] objectForKey:@"statusName"];
        [cell.business setTitle:[arySearchedServices[indexPath.row] objectForKey:@"fenleiName"] forState:UIControlStateNormal];
        cell.itemID = [arySearchedServices[indexPath.row] objectForKey:@"id"];
        [cell setIsUp:[[arySearchedServices[indexPath.row] objectForKey:@"status"] boolValue]];
        //cell.business.text = [aryServices[indexPath.row] objectForKey:@"statusName"];
        return cell;

    }
    
    return nil;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(selected_tab == em_Product)
    {
       // HomeCommerceDetailViewController* vc = [[HomeCommerceDetailViewController alloc] initWithNibName:@"HomeCommerceDetailViewController" bundle:nil];
       // [[CommonData sharedInstance] setSelectedProductID:[arySearchedProducts[indexPath.row] objectForKey:@"id"]];
        [CommonData sharedInstance].selectedProductID = [arySearchedProducts[indexPath.row] objectForKey:@"id"];
        ProductDetailsViewController* vc = [[ProductDetailsViewController alloc] initWithNibName:@"ProductDetailsViewController" bundle:nil];
        [self.navigationController pushViewController:vc animated:YES];
    }else if(selected_tab == em_Item)
    {
        ItemDetailViewController* vc = [[ItemDetailViewController alloc] initWithNibName:@"ItemDetailViewController" bundle:nil];
        [[CommonData sharedInstance] setSelectedItemServiceDic:arySearchedItems[indexPath.row]];
        [CommonData sharedInstance].detailItemServiceIndex = SUB_HOME_ITEM;
        [self.navigationController pushViewController:vc animated:YES];
    }else if(selected_tab == em_Service)
    {
        ItemDetailViewController* vc = [[ItemDetailViewController alloc] initWithNibName:@"ItemDetailViewController" bundle:nil];
        [[CommonData sharedInstance] setSelectedItemServiceDic:arySearchedServices[indexPath.row]];
        [CommonData sharedInstance].detailItemServiceIndex = SUB_HOME_SERVICE;
        [self.navigationController pushViewController:vc animated:YES];
    }
}

#pragma mark - Actions
- (IBAction)onProduct:(id)sender
{
    [self setTab:em_Product];
    [self refreshTopItems];
}
- (IBAction)onItem:(id)sender
{
    [self setTab:em_Item];
    [self refreshTopItems];
}
- (IBAction)onService:(id)sender
{
    [self setTab:em_Service];
    [self refreshTopItems];
}
- (IBAction)onBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}
- (IBAction)onKeyboardReturn:(id)sender
{
    [self.view endEditing:YES];
    self.btnKeyboardReturn.hidden = YES;
}
- (IBAction)onWrite:(id)sender
{
    [CommonData sharedInstance].lastClick = @"SearchProductsViewController";
    if(selected_tab == em_Product)
    {
        HomeCommerceAddViewController* vc = [[HomeCommerceAddViewController alloc] initWithNibName:@"HomeCommerceAddViewController" bundle:nil];
        [self.navigationController pushViewController:vc animated:YES];
    }else if(selected_tab == em_Item)
    {
        HomeItemAddViewController* vc = [[HomeItemAddViewController alloc] initWithNibName:@"HomeItemAddViewController" bundle:nil];
        [CommonData sharedInstance].addItemServiceIndex = ITEM_PAGE;
        [self.navigationController pushViewController:vc animated:YES];
    }else if(selected_tab == em_Service)
    {
        HomeItemAddViewController* vc = [[HomeItemAddViewController alloc] initWithNibName:@"HomeItemAddViewController" bundle:nil];
        [CommonData sharedInstance].addItemServiceIndex = SERVICE_PAGE;
        [self.navigationController pushViewController:vc animated:YES];
    }
}
#pragma mark - Search Bar
- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText
{
    if(searchText.length == 0)
    {
        
        [arySearchedProducts removeAllObjects];
        [arySearchedItems removeAllObjects];
        [arySearchedServices removeAllObjects];
        
        [arySearchedProducts addObjectsFromArray:aryProducts];
        [arySearchedItems addObjectsFromArray:aryItems];
        [arySearchedServices addObjectsFromArray:aryServices];
        [self.tableView reloadData];
        return;
    }

    {
        [arySearchedProducts removeAllObjects];
        for(int i = 0; i < aryProducts.count; i++)
        {
            NSDictionary* dic = aryProducts[i];
            if([dic[@"name"] containsString:searchText] == YES)
            {
                [arySearchedProducts addObject:dic];
            }
        }
    }
    {
        [arySearchedItems removeAllObjects];
        for(int i = 0; i < aryItems.count; i++)
        {
            NSDictionary* dic = aryItems[i];
            if([dic[@"name"] containsString:searchText] == YES || [dic[@"comment"] containsString:searchText] == YES)
            {
                [arySearchedItems addObject:dic];
            }
        }
    }
    {
        [arySearchedServices removeAllObjects];
        for(int i = 0; i < aryServices.count; i++)
        {
            NSDictionary* dic = aryServices[i];
            if([dic[@"name"] containsString:searchText] == YES || [dic[@"comment"] containsString:searchText] == YES)
            {
                [arySearchedServices addObject:dic];
            }
        }
    }
    [self.tableView reloadData];
}

- (BOOL)searchBarShouldBeginEditing:(UISearchBar *)searchBar
{
    self.btnKeyboardReturn.hidden = NO;
    return YES;
}
- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar
{
    
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar
{
   // [self getList:selected_tab For:searchBar.text];
    [self.view endEditing:YES];
}

#pragma mark
-(void)itemEditing:(UITableViewCell *)cell
{
    [CommonData sharedInstance].lastClick = @"SearchProductsViewController";
    if(selected_tab == em_Product)
    {
        HomeCommerceAddViewController* vc = [[HomeCommerceAddViewController alloc] initWithNibName:@"HomeCommerceAddViewController" bundle:nil];
        vc.changedProductID = [((ProductTableViewCell*)cell).productID stringValue];
        
        for(int i = 0; i < aryProducts.count; i++)
        {
            NSDictionary* item = aryProducts[i];
            if([[item objectForKey:@"id"] intValue] == [((ProductTableViewCell*)cell).productID intValue])
            {
                vc.product = item;
                break;
            }
        }
        
        [self.navigationController pushViewController:vc animated:YES];
    }else if(selected_tab == em_Item)
    {
        HomeItemAddViewController* vc = [[HomeItemAddViewController alloc] initWithNibName:@"HomeItemAddViewController" bundle:nil];
        [CommonData sharedInstance].addItemServiceIndex = ITEM_PAGE;
        for(int i = 0; i < aryItems.count; i++)
        {
            NSDictionary* item = aryItems[i];
            if([[item objectForKey:@"id"] intValue] == [((ItemTableViewCell*)cell).itemID intValue])
            {
                vc.item = item;
                break;
            }
        }
        [self.navigationController pushViewController:vc animated:YES];
    }else if(selected_tab == em_Service)
    {
        HomeItemAddViewController* vc = [[HomeItemAddViewController alloc] initWithNibName:@"HomeItemAddViewController" bundle:nil];
        [CommonData sharedInstance].addItemServiceIndex = SERVICE_PAGE;
        for(int i = 0; i < aryServices.count; i++)
        {
            NSDictionary* item = aryServices[i];
            if([[item objectForKey:@"id"] intValue] == [((ItemTableViewCell*)cell).itemID intValue])
            {
                vc.item = item;
                break;
            }
        }
        
        [self.navigationController pushViewController:vc animated:YES];
    }
}
-(void)itemDeleting:(UITableViewCell *)cell
{
    currentCell = cell;
    if(selected_tab == em_Product)
    {
        currentID = ((ProductTableViewCell*)cell).productID;
    }else
    {
        currentID = ((ItemTableViewCell*)cell).itemID;
    }
    if(selected_tab == em_Product)
    {
        deleteAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要删除该产品吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        [deleteAlert show];
    }else if(selected_tab == em_Item)
    {
        deleteAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要删除该项目吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        [deleteAlert show];
    }else if(selected_tab == em_Service)
    {
        deleteAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要删除该服务吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        [deleteAlert show];
    }
    
}
-(void)alertViewCancel:(UIAlertView *)alertView
{
    
}
-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 1)
    {
        if(deleteAlert == alertView)
        {
            if(selected_tab == em_Product)
            {
                NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
                [dicParams setObject:ACTION_DELETEPRODUCT forKey:@"pAct"];
                [dicParams setObject:currentID forKey:@"productId"];
                [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
                
                [[WebAPI sharedInstance] sendPostRequest:ACTION_DELETEPRODUCT Parameters:dicParams :^(NSObject *resObj){
                    
                    [GeneralUtil hideProgress];
                    
                    NSDictionary *dicRes = (NSDictionary *)resObj;
                    
                    if (dicRes != nil ) {
                        if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                            [self refreshTopItems];
                        }
                        else {
                            [appDelegate.window makeToast:dicRes[@"msg"]
                                        duration:3.0
                                        position:CSToastPositionCenter
                                           style:nil];
                        }
                    }
                }];
                
            }else if(selected_tab == em_Item)
            {
                NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
                [dicParams setObject:ACTION_DELETEITEM forKey:@"pAct"];
                [dicParams setObject:currentID forKey:@"itemId"];
                [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
                
                [[WebAPI sharedInstance] sendPostRequest:ACTION_DELETEITEM Parameters:dicParams :^(NSObject *resObj){
                    
                    [GeneralUtil hideProgress];
                    
                    NSDictionary *dicRes = (NSDictionary *)resObj;
                    
                    if (dicRes != nil ) {
                        if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                            [self refreshTopItems];
                        }
                        else {
                            [appDelegate.window makeToast:dicRes[@"msg"]
                                        duration:3.0
                                        position:CSToastPositionCenter
                                           style:nil];
                        }
                    }
                }];
            }else if(selected_tab == em_Service)
            {
                NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
                [dicParams setObject:ACTION_DELETESERVICE forKey:@"pAct"];
                [dicParams setObject:currentID forKey:@"serviceId"];
                [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
                
                [[WebAPI sharedInstance] sendPostRequest:ACTION_DELETESERVICE Parameters:dicParams :^(NSObject *resObj){
                    
                    [GeneralUtil hideProgress];
                    
                    NSDictionary *dicRes = (NSDictionary *)resObj;
                    
                    if (dicRes != nil ) {
                        if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                            [self refreshTopItems];
                        }
                        else {
                            [appDelegate.window makeToast:dicRes[@"msg"]
                                        duration:3.0
                                        position:CSToastPositionCenter
                                           style:nil];
                        }
                    }
                }];
            }

        }else if(alertView == upDownAlert)
        {
            if(selected_tab == em_Product)
            {
                BOOL isUp = ((ProductTableViewCell*)currentCell).isUp;
                NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
                [dicParams setObject:isUp ? ACTION_DOWNPRODUCT : ACTION_UPPRODUCT forKey:@"pAct"];
                [dicParams setObject:currentID forKey:@"productId"];
                [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
                
                [[WebAPI sharedInstance] sendPostRequest:isUp ? ACTION_DOWNPRODUCT : ACTION_UPPRODUCT Parameters:dicParams :^(NSObject *resObj){
                    
                    [GeneralUtil hideProgress];
                    
                    NSDictionary *dicRes = (NSDictionary *)resObj;
                    
                    if (dicRes != nil ) {
                        if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                            [self refreshTopItems];
                        }
                        else {
                            [appDelegate.window makeToast:dicRes[@"msg"]
                                        duration:3.0
                                        position:CSToastPositionCenter
                                           style:nil];
                        }
                    }
                }];
                
            }else if(selected_tab == em_Item)
            {
                BOOL isUp = ((ItemTableViewCell*)currentCell).isUp;
                NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
                [dicParams setObject:isUp ? ACTION_DOWNITEM : ACTION_UPITEM forKey:@"pAct"];
                [dicParams setObject:currentID forKey:@"itemId"];
                [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
                
                [[WebAPI sharedInstance] sendPostRequest:isUp ? ACTION_DOWNITEM : ACTION_UPITEM Parameters:dicParams :^(NSObject *resObj){
                    
                    [GeneralUtil hideProgress];
                    
                    NSDictionary *dicRes = (NSDictionary *)resObj;
                    
                    if (dicRes != nil ) {
                        if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                            [self refreshTopItems];
                        }
                        else {
                            [appDelegate.window makeToast:dicRes[@"msg"]
                                        duration:3.0
                                        position:CSToastPositionCenter
                                           style:nil];
                        }
                    }
                }];
                
            }else if(selected_tab == em_Service)
            {
                BOOL isUp = ((ItemTableViewCell*)currentCell).isUp;
                NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
                [dicParams setObject:isUp ? ACTION_DOWNSERVICE : ACTION_UPSERVICE forKey:@"pAct"];
                [dicParams setObject:currentID forKey:@"serviceId"];
                [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
                
                [[WebAPI sharedInstance] sendPostRequest:isUp ? ACTION_UPSERVICE : ACTION_DOWNSERVICE Parameters:dicParams :^(NSObject *resObj){
                    
                    [GeneralUtil hideProgress];
                    
                    NSDictionary *dicRes = (NSDictionary *)resObj;
                    
                    if (dicRes != nil ) {
                        if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                            [self refreshTopItems];
                        }
                        else {
                            [appDelegate.window makeToast:dicRes[@"msg"]
                                        duration:3.0
                                        position:CSToastPositionCenter
                                           style:nil];
                        }
                    }
                }];
                
            }
        }
    }
}
-(void)itemUpDown:(UITableViewCell *)cell
{
    currentCell = cell;
    BOOL isUp;
    if(selected_tab == em_Product)
    {
        currentID = ((ProductTableViewCell*)cell).productID;
        isUp = ((ProductTableViewCell*)cell).isUp;
    }else
    {
        currentID = ((ItemTableViewCell*)cell).itemID;
        isUp = ((ItemTableViewCell*)cell).isUp;
    }
    
    if(selected_tab == em_Product)
    {
        if(isUp)
        {
            upDownAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要下架该产品吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        }else
        {
            upDownAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要上架该产品吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        }
        
        [upDownAlert show];
    }else if(selected_tab == em_Item)
    {
        if(isUp)
        {
            upDownAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要下架该项目吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        }else
        {
            upDownAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要上架该项目吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        }
        
        [upDownAlert show];
    }else if(selected_tab == em_Service)
    {
        if(isUp)
        {
            upDownAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要下架该服务吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        }else
        {
            upDownAlert = [[UIAlertView alloc] initWithTitle:@"" message:@"确定要上架该服务吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        }
        
        [upDownAlert show];
    }
    
    
}
@end
