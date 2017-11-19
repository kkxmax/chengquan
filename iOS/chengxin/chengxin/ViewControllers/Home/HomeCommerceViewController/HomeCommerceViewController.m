//
//  HomeCommerceViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeCommerceViewController.h"
#import "HomeCommerceCollectionViewCell.h"
#import "Global.h"
#import "UIImageView+WebCache.h"

@interface HomeCommerceViewController ()
{
    NSMutableArray *productArray;
}
@end

@implementation HomeCommerceViewController
@synthesize commerceCollectionView, indicatorView, currentSortOrderIndex;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    productArray = [NSMutableArray array];
    [self.commerceCollectionView registerNib:[UINib nibWithNibName:@"HomeCommerceCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:@"HomeCommerceCellIdentifier"];
    currentSortOrderIndex = 1;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2) {
        self.addButton.hidden = YES;
    }else{
        self.addButton.hidden = NO;
        int aKind = [[CommonData sharedInstance].userInfo[@"akind"] intValue];
        if(aKind == 1) {
            self.addButton.hidden = YES;
        }
    }
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}

- (void)getProductFromServer:cityName PleixingIds:(NSString*)pleixingIds Start:(NSString*)start Length:(NSString*)length Keyword:(NSString*)keyword  {
    indicatorView.hidden = NO;
    [indicatorView startAnimating];
    [productArray removeAllObjects];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getProductList" forKey:@"pAct"];
    [dicParams setObject:start forKey:@"start"];
    [dicParams setObject:length forKey:@"length"];
    [dicParams setObject:pleixingIds forKey:@"pleixingIds"];
    [dicParams setObject:[NSString stringWithFormat:@"%ld", (long)currentSortOrderIndex] forKey:@"order"];
    [dicParams setObject:cityName forKey:@"cityName"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETPRODUCTLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                NSArray *productList = (NSArray *)(dicRes[@"data"]);
                for(int i = 0; i < productList.count; i++) {
                    NSDictionary *productDic = (NSDictionary *)(productList[i]);
                    [productArray addObject:productDic];
                }
                [self.commerceCollectionView reloadData];
                [indicatorView stopAnimating];
            }
        }
    }];
}

#pragma mark - UICollectionView Delegate, DataSource
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    HomeCommerceCollectionViewCell *homeCommerceCollectionCell = (HomeCommerceCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:@"HomeCommerceCellIdentifier" forIndexPath:indexPath];
    NSDictionary *productDic = (NSDictionary *)[productArray objectAtIndex:indexPath.row];
    NSString *productImageName = productDic[@"imgPath1"];
    [homeCommerceCollectionCell.produceImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", BASE_WEB_URL, productImageName]]];
    homeCommerceCollectionCell.produceNameLabel.text = productDic[@"name"];
    homeCommerceCollectionCell.producePriceLabel.text = [NSString stringWithFormat:@"%ld", (long)[productDic[@"price"] longValue]];
    return homeCommerceCollectionCell;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return productArray.count;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
    {
        CGSize result = [[UIScreen mainScreen] bounds].size;
        if(result.height == 568)
        {
            return CGSizeMake(156.f, 200);
            
        }
        if(result.height == 667)
        {
            return CGSizeMake(184.f, 244);
            
        }
        if(result.height == 736)
        {
            return CGSizeMake(202.f, 244);
            
            
        }
    }
    return CGSizeMake(184.f, 244);
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    int nTestStatus = [[CommonData sharedInstance].userInfo[@"testStatus"] intValue];
    if(nTestStatus != 2)
        return;
    NSDictionary *productDic = (NSDictionary *)[productArray objectAtIndex:indexPath.row];
    [CommonData sharedInstance].selectedProductID = [NSString stringWithFormat:@"%ld", [productDic[@"id"] longValue]];
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMECOMMERCEDETAIL_VIEW_NOTIFICATION object:indexPath];
}

- (IBAction)addAction:(id)sender {
    [[NSNotificationCenter defaultCenter] postNotificationName:SHOW_HOMEPRODUCTADD_VIEW_NOTIFICATION object:nil];
}


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
