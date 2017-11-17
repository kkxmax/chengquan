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

@interface HomeCommerceViewController ()

@end

@implementation HomeCommerceViewController
@synthesize slideCommerceScrollView, slideCommercePageCtrl, commerceCollectionView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self.commerceCollectionView registerNib:[UINib nibWithNibName:@"HomeCommerceCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:@"HomeCommerceCellIdentifier"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    // Set slide page time
    [self showSlideShow];
    slideCommercePageTimer = [NSTimer scheduledTimerWithTimeInterval:SLIDE_SECOND target:self selector:@selector(PageMove) userInfo:nil repeats:YES];
}

#pragma mark - Page Slide
- (void)showSlideShow {
    slideCommerceScrollView.contentSize = CGSizeMake(slideCommerceScrollView.frame.size.width * NUMBER_OF_SLIDE_IMAGES, slideCommerceScrollView.frame.size.height);
    CGRect ViewSize = slideCommerceScrollView.bounds;
    for(int i = 0; i <= NUMBER_OF_SLIDE_IMAGES; i++)
    {
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:ViewSize];
        imageView.contentMode = UIViewContentModeScaleToFill;
        [imageView setImage:[UIImage imageNamed:[NSString stringWithFormat:@"%d.png", (COMMERCE_IMAGE_START + i)]]];
        [slideCommerceScrollView addSubview:imageView];
        ViewSize = CGRectOffset(ViewSize, slideCommerceScrollView.bounds.size.width, 0);
    }
}
- (void)PageMove {
    CGFloat pageSize = slideCommerceScrollView.frame.size.width;
    NSInteger nCurrentPage = 0;
    // if this is the last page return
    if(slideCommerceScrollView.contentOffset.x >= slideCommerceScrollView.frame.size.width * NUMBER_OF_SLIDE_IMAGES) {
        [slideCommerceScrollView setContentOffset:CGPointMake(0.0, slideCommerceScrollView.contentOffset.y) animated:YES];
    } else {
        [slideCommerceScrollView setContentOffset:CGPointMake(slideCommerceScrollView.contentOffset.x + pageSize, slideCommerceScrollView.contentOffset.y) animated:YES];
        nCurrentPage = (NSInteger)(slideCommerceScrollView.contentOffset.x / slideCommerceScrollView.frame.size.width) + 1;
    }
    [slideCommercePageCtrl setCurrentPage:nCurrentPage];
}

#pragma mark - UICollectionView Delegate, DataSource
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
//    HomeCommerceCollectionViewCell *homeCommerceCollectionCell = (HomeCommerceCollectionViewCell *)[[[NSBundle mainBundle] loadNibNamed:@"HomeCommerceCollectionViewCell" owner:self options:nil] objectAtIndex:0];
    HomeCommerceCollectionViewCell *homeCommerceCollectionCell = (HomeCommerceCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:@"HomeCommerceCellIdentifier" forIndexPath:indexPath];
    return homeCommerceCollectionCell;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return 5;
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
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
